package sk.tuke.gamestudio.game.jigsawsudoku.core;

public class Cell {
  private final int row;
  private final int col;
  private int value; // 0 - empty
  private final CellState state;

  public Cell(int row, int col, int value, CellState state) {
    this.row = row;
    this.col = col;
    this.value = value;
    this.state = state;
  }

  public void setValue(int value) {
    this.value = value;
  }
  public int getValue() { return value; }
  public int getRow() { return row; }
  public int getCol() { return col; }
  public CellState getState() { return state; }

  public boolean isFixed() { return state == CellState.FIXED; }
}
