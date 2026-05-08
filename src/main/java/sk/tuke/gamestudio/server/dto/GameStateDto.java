package sk.tuke.gamestudio.server.dto;

public class GameStateDto {
  private String gameId;
  private int size;
  private int[][] board;
  private boolean[][] fixedCells;
  private int[][] regions;
  private String difficulty;
  private String state;
  private boolean solvedAutomatically;
  private boolean lastMoveValid;
  private String message;
  private int hintsLeft;
  private long elapsedSeconds;
  private boolean scoreSubmitted;
  private int currentScore;

  public GameStateDto() {}
  public GameStateDto(
          String gameId,
          int size,
          int[][] board,
          boolean[][] fixedCells,
          int[][] regions,
          String difficulty,
          String state,
          boolean solvedAutomatically,
          boolean lastMoveValid,
          String message,
          int hintsLeft,
          long elapsedSeconds,
          boolean scoreSubmitted,
          int currentScore
  ) {
    this.gameId = gameId;
    this.size = size;
    this.board = board;
    this.fixedCells = fixedCells;
    this.regions = regions;
    this.difficulty = difficulty;
    this.state = state;
    this.solvedAutomatically = solvedAutomatically;
    this.lastMoveValid = lastMoveValid;
    this.message = message;
    this.hintsLeft = hintsLeft;
    this.elapsedSeconds = elapsedSeconds;
    this.scoreSubmitted = scoreSubmitted;
    this.currentScore = currentScore;
  }

  public String getGameId() { return gameId; }
  public void setGameId(String gameId) { this.gameId = gameId; }
  public int getSize() { return size; }
  public void setSize(int size) { this.size = size; }
  public int[][] getBoard() { return board; }
  public void setBoard(int[][] board) { this.board = board; }
  public boolean[][] getFixedCells() { return fixedCells; }
  public void setFixedCells(boolean[][] fixedCells) { this.fixedCells = fixedCells; }
  public int[][] getRegions() { return regions; }
  public void setRegions(int[][] regions) { this.regions = regions; }
  public String getDifficulty() { return difficulty; }
  public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
  public String getState() { return state; }
  public void setState(String state) { this.state = state; }
  public boolean isSolvedAutomatically() { return solvedAutomatically; }
  public void setSolvedAutomatically(boolean solvedAutomatically) { this.solvedAutomatically = solvedAutomatically; }
  public boolean isLastMoveValid() { return lastMoveValid; }
  public void setLastMoveValid(boolean lastMoveValid) { this.lastMoveValid = lastMoveValid; }
  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
  public int getHintsLeft() { return hintsLeft; }
  public void setHintsLeft(int hintsLeft) { this.hintsLeft = hintsLeft; }
  public long getElapsedSeconds() { return elapsedSeconds; }
  public void setElapsedSeconds(long elapsedSeconds) { this.elapsedSeconds = elapsedSeconds; }
  public boolean isScoreSubmitted() { return scoreSubmitted; }
  public void setScoreSubmitted(boolean scoreSubmitted) { this.scoreSubmitted = scoreSubmitted; }
  public int getCurrentScore() { return currentScore; }
  public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
}
