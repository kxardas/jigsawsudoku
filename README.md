# Odkaz na video k prvému odovzdaniu
https://www.youtube.com/watch?v=SItLRdFK4Eg

# Odkaz na video k druhému odovzdaniu
https://youtu.be/B9uoSw7nFGc?si=bCi0I5K8mvF_ORAn

* `docker compose up -d` - run redis
* `mvn spring-boot:run` - run project
# Jigsaw Sudoku GameStudio

Full-stack Jigsaw Sudoku web app built as a GameStudio project.  
The goal was not only to make a playable Sudoku, but also to add proper accounts, score handling, comments, ratings, hints, notes and basic backend security.

## What is inside

- Jigsaw Sudoku boards: `5x5`, `7x7`, `9x9`
- difficulty levels: `Easy`, `Normal`, `Hard`
- interactive board with region outlines
- pencil marks / notes mode
- limited hints per game
- timer and backend-calculated score
- victory modal with final result
- leaderboard
- comments and rating
- registration and login
- JWT auth stored in HttpOnly cookies
- Redis-backed rate limiting for login/register
- protected comment, rating and score submit endpoints
- game restore after page refresh while the backend session still exists

## Tech stack

### Frontend

- React
- TypeScript
- Vite
- Tailwind CSS
- Axios
- Framer Motion
- Lucide React

### Backend

- Java
- Spring Boot
- JPA / JDBC services
- PostgreSQL
- Redis
- JWT
- HttpOnly cookies

## Main gameplay

The game creates a Jigsaw Sudoku puzzle on the backend and returns the current game state to the frontend.  
The frontend renders the board, selected cell, notes, number pad, hints, timer and game status.

A game session is stored on the backend. It keeps the board, hint count, start time, solved time and score submit state.  
Because of that, score is not trusted from the frontend.

Score is calculated on the backend using:

- board size
- difficulty
- elapsed time
- hints used

This prevents users from sending fake points through DevTools or Postman.

## Authentication

Authentication uses JWT, but the token is not stored in `localStorage`.  
It is stored in an HttpOnly cookie, so JavaScript cannot directly read it.

Implemented auth endpoints:

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
POST /api/auth/logout
```

The frontend checks the current user through `/api/auth/me` after loading the app.

## Security notes

Some endpoints are public, some require authentication.

Public:

```text
GET /api/comment/{game}
GET /api/rating/{game}
GET /api/score/{game}
```

Protected:

```text
POST /api/comment
POST /api/rating
POST /api/engine/games/{gameId}/score
```

For protected actions, the backend takes the username from the authenticated cookie.  
It does not trust `username`, `player` or `points` sent from the frontend.

The old direct score submit endpoint is blocked:

```text
POST /api/score
```

Scores should be submitted only through:

```text
POST /api/engine/games/{gameId}/score
```

## Rate limiting

Login and register endpoints are protected with Redis-backed rate limiting.

Example limits:

- limited registration attempts per IP
- limited login attempts per IP
- limited login attempts per username

This is mainly to prevent account-spam bots and simple brute-force attempts.

## Running the project

### 1. Start Redis

From the project root:

```bash
docker compose up -d
```

Redis is needed for rate limiting.

### 2. Start the backend

Run the Spring Boot server from your IDE or with Maven.

Example:

```bash
mvn spring-boot:run
```

The backend expects PostgreSQL to be available with the configured database credentials.

### 3. Start the frontend

From the frontend folder:

```bash
npm install
npm run dev
```

Then open the Vite dev server URL in the browser.

## Environment / configuration

Redis configuration is stored in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

Database configuration depends on the local PostgreSQL setup used for the GameStudio project.

## Project structure

The backend is split into:

```text
entity/                 domain entities
service/                JPA/JDBC service implementations
server/dto/             REST DTOs
server/service/         game/auth/server logic
server/webservice/      REST controllers
game/jigsawsudoku/      core game logic
```

The frontend is split into:

```text
api/                    Axios API calls
components/auth/        login/register/user UI
components/game/        board, controls, HUD, victory modal
components/layout/      page layout/header
components/ui/          reusable UI components
context/                auth and toast context
hooks/                  game/comments/rating/leaderboard logic
types/                  TypeScript types
utils/                  constants, date/time, error helpers
```

## Current limitations

- active game sessions are stored in backend memory, so they disappear after backend restart
- OAuth buttons are prepared visually, but OAuth itself is not implemented yet
- Redis must be running before using auth rate-limited endpoints
- this is still a university project, not a production deployment

## Useful test cases

Before presenting the project, check these manually:

```text
guest can play
guest cannot comment/rate/submit score
logged-in user can comment
logged-in user can rate
logged-in user can submit score after solving manually
auto-solved game cannot be submitted
duplicate score submit is rejected
leaderboard updates after score submit
refresh during a game restores the active game
too many login/register attempts returns 429
```

## Author

kxardas
