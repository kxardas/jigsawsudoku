package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.game.jigsawsudoku.core.Board;

import java.util.Date;

public class GameSession {
  public static final int MAX_HINTS = 3;

  private final Board board;
  private int hintsLeft;
  private boolean scoreSubmitted;

  private final Date startedAt;
  private Date solvedAt;

  public GameSession(Board board) {
    this.board = board;
    hintsLeft = MAX_HINTS;
    scoreSubmitted = false;
    startedAt = new Date();
  }

  public Board getBoard() { return board; }

  public int getHintsLeft() { return hintsLeft; }

  public int getHintsUsed() { return MAX_HINTS - hintsLeft; }

  public void useHint() {
    if (hintsLeft <= 0) {
      throw new IllegalStateException("No hints left");
    }

    hintsLeft--;
  }

  public boolean isScoreSubmitted() { return scoreSubmitted; }

  public void markScoreSubmitted() { scoreSubmitted = true; }

  public Date getStartedAt() { return startedAt; }

  public Date getSolvedAt() { return solvedAt; }

  public void markSolved() {
    if (solvedAt == null) {
      solvedAt = new Date();
    }
  }

  public long getElapsedSeconds() {
    Date end = solvedAt != null ? solvedAt : new Date();

    return Math.max(0, (end.getTime() - startedAt.getTime()) / 1000);
  }
}
