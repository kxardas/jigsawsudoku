package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.game.jigsawsudoku.core.Board;
import sk.tuke.gamestudio.game.jigsawsudoku.core.Difficulty;
import sk.tuke.gamestudio.game.jigsawsudoku.core.Puzzle;
import sk.tuke.gamestudio.game.jigsawsudoku.core.PuzzleRepository;
import sk.tuke.gamestudio.server.dto.GameStateDto;
import sk.tuke.gamestudio.server.dto.MoveDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JigsawSudokuGameService {
  private final PuzzleRepository puzzleRepository = new PuzzleRepository();
  private final Random random = new Random();
  private final Map<String, GameSession> games = new ConcurrentHashMap<>();
  private final ScoreCalculator scoreCalculator;

  public JigsawSudokuGameService(ScoreCalculator scoreCalculator) {
    this.scoreCalculator = scoreCalculator;
  }

  public GameStateDto createGame(int size, String difficultyValue) {
    validateSize(size);

    Difficulty difficulty = parseDifficulty(difficultyValue);

    Puzzle puzzle = puzzleRepository.getRandomPuzzle(size, difficulty);
    Board board = new Board(puzzle);

    String gameId = UUID.randomUUID().toString();
    games.put(gameId, new GameSession(board));

    return toDto(gameId, board, true, "Game created");
  }

  private void validateSize(int size) {
    if (size != 5 && size != 7 && size != 9) {
      throw new IllegalArgumentException("Unsupported board size: " + size);
    }
  }

  public GameSession getSession(String gameId) {
    return getSessionOrThrow(gameId);
  }

  public GameStateDto getGame(String gameId) {
    Board board = getBoardOrThrow(gameId);
    return toDto(gameId, board, true, "Game loaded");
  }

  public GameStateDto makeMove(String gameId, MoveDto moveDto) {
    if (moveDto == null) {
      throw new IllegalArgumentException("Move request cannot be empty");
    }

    Board board = getBoardOrThrow(gameId);

    boolean valid = board.setValue(
            moveDto.getRow(),
            moveDto.getCol(),
            moveDto.getValue()
    );

    markSolvedIfNeeded(gameId, board);

    String message = valid ? "Move accepted" : "Invalid move";

    return toDto(gameId, board, valid, message);
  }

  public GameStateDto clearCell(String gameId, int row, int col) {
    Board board = getBoardOrThrow(gameId);

    boolean valid = board.clearValue(row, col);
    String message = valid ? "Cell cleared" : "Cannot clear cell";

    return toDto(gameId, board, valid, message);
  }

  public GameStateDto solveGame(String gameId) {
    Board board = getBoardOrThrow(gameId);

    board.solve();

    markSolvedIfNeeded(gameId, board);

    return toDto(gameId, board, true, "Game solved");
  }

  public GameStateDto resetBoard(String gameId) {
    Board board = getBoardOrThrow(gameId);

    board.reset();

    return toDto(gameId, board, true, "Board was cleared");
  }

  private GameSession getSessionOrThrow(String gameId) {
    GameSession session = games.get(gameId);

    if (session == null) {
      throw new IllegalArgumentException("No such game: " + gameId);
    }

    return session;
  }

  private Board getBoardOrThrow(String gameId) {
    return getSessionOrThrow(gameId).getBoard();
  }

  private void markSolvedIfNeeded(String gameId, Board board) {
    if (board.getState().name().equals("SOLVED")) {
      getSessionOrThrow(gameId).markSolved();
    }
  }

  private Difficulty parseDifficulty(String difficultyValue) {
    if (difficultyValue == null || difficultyValue.isEmpty()) { return Difficulty.EASY; }
    return Difficulty.valueOf(difficultyValue.toUpperCase());
  }

  private GameStateDto toDto(String gameId, Board board, boolean lastMoveValid, String message) {
    GameSession session = getSessionOrThrow(gameId);
    int hintsLeft = session.getHintsLeft();

    int currentScore = scoreCalculator.calculateScore(
            board.getSize(),
            board.getDifficulty(),
            session.getElapsedSeconds(),
            session.getHintsUsed()
    );

    int size = board.getSize();

    int[][] values = new int[size][size];
    boolean[][] fixedCells = new boolean[size][size];
    int[][] regions = new int[size][size];

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        values[row][col] = board.getCell(row, col).getValue();
        fixedCells[row][col] = board.getCell(row, col).isFixed();
        regions[row][col] = board.getRegionId(row, col);
      }
    }

    return new GameStateDto(
      gameId,
      size,
      values,
      fixedCells,
      regions,
      board.getDifficulty().name(),
      board.getState().name(),
      board.getIsSolvedAutomatically(),
      lastMoveValid,
      message,
      hintsLeft,
      session.getElapsedSeconds(),
      session.isScoreSubmitted(),
      currentScore
    );
  }

  public GameStateDto useHint(String gameId) {
    GameSession session = getSessionOrThrow(gameId);
    Board board = session.getBoard();

    if (!board.getState().name().equals("PLAYING")) {
      throw new IllegalStateException("Hints are only available while playing");
    }

    if (session.getHintsLeft() <= 0) {
      throw new IllegalStateException("No hints left");
    }

    int size = board.getSize();
    List<int[]> candidates = new ArrayList<>();

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        boolean isEditable = !board.getCell(row, col).isFixed();
        boolean isEmpty = board.getCell(row, col).getValue() == 0;

        int solutionValue = board.getSolutionValue(row, col);

        if (isEditable && isEmpty && board.isMoveAllowed(row, col, solutionValue)) {
          candidates.add(new int[] { row, col });
        }
      }
    }

    if (candidates.isEmpty()) {
      throw new IllegalStateException("No hints available");
    }

    int[] selectedCell = candidates.get(random.nextInt(candidates.size()));

    int row = selectedCell[0];
    int col = selectedCell[1];
    int value = board.getSolutionValue(row, col);

    if (!board.setValue(row, col, value)) {
      throw new IllegalStateException("No hints available");
    }

    session.useHint();

    markSolvedIfNeeded(gameId, board);

    return toDto(gameId, board, true, "Hint used");
  }
}
