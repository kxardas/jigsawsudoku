package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.game.jigsawsudoku.core.Difficulty;

public class ScoreCalculator {
  public int calculateScore(int size, Difficulty difficulty, long elapsedSeconds, int hintsUsed) {
    double baseScore = getBaseScore(size) * getDifficultyScoreMultiplier(difficulty);
    double targetSeconds = getTargetSeconds(size) * getDifficultyTimeMultiplier(difficulty);

    double timeMultiplier = Math.max(
            0.3,
            1.0 - ((double) elapsedSeconds / targetSeconds)
    );

    double hintPenalty = baseScore * 0.12 * hintsUsed;

    double finalScore = baseScore * timeMultiplier - hintPenalty;

    return Math.max((int) Math.round(finalScore), 0);
  }

  private int getBaseScore(int size) {
    return switch(size){
      case 5 -> 300;
      case 7 -> 600;
      case 9 -> 1000;
      default -> throw new IllegalArgumentException("Unsupported board size:" + size);
    };
  }

  private double getDifficultyScoreMultiplier(Difficulty difficulty) {
    return switch(difficulty) {
      case EASY -> 1.0;
      case NORMAL -> 1.35;
      case HARD -> 1.75;
    };
  }

  private int getTargetSeconds(int size) {
    return switch(size) {
      case 5 -> 180;
      case 7 -> 420;
      case 9 -> 900;
      default -> throw new IllegalArgumentException("Unsupported board size:" + size);
    };
  }

  private double getDifficultyTimeMultiplier(Difficulty difficulty) {
    return switch(difficulty) {
      case EASY -> 1.0;
      case NORMAL -> 1.25;
      case HARD -> 1.5;
    };
  }
}
