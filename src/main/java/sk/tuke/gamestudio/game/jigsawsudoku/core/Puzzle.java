package sk.tuke.gamestudio.game.jigsawsudoku.core;

public class Puzzle {
  private final int size;
  private final int[][] regionsId;
  private final int[][] initialValues;
  private final int[][] solution;
  private final Difficulty difficulty;

  public Puzzle(int size, int[][] regionsId, int[][] initialValues, int[][] solution, Difficulty difficulty) {
    this.size = size;
    this.regionsId = regionsId;
    this.initialValues = initialValues;
    this.solution = solution;
    this.difficulty = difficulty;
  }

  public int getSize() {
    return size;
  }

  public int[][] getRegionsId() {
    return regionsId;
  }

  public int[][] getInitialValues() {
    return initialValues;
  }

  public int[][] getSolution() {
    return solution;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }
}
