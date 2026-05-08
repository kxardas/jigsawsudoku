package sk.tuke.gamestudio.game.jigsawsudoku.core;

import sk.tuke.gamestudio.game.jigsawsudoku.consoleui.Ansi;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private final int size;
  private Cell[][] cells;
  private final int[][] regionsId;
  private final Cell[][] cellsSolved;
  private final Difficulty difficulty;
  private GameState state = GameState.PLAYING;
  private boolean isSolvedAutomatically = false;

  public Board(int size, int[][] regionsId, int[][] initialValues, Difficulty difficulty, int[][] solved) {
    if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");
    if (regionsId == null || initialValues == null || solved == null) throw new IllegalArgumentException("Regions id, initial values and solution must not be null");
    if (!hasSquareShape(regionsId, size) || !hasSquareShape(initialValues, size) || !hasSquareShape(solved, size)) throw new IllegalArgumentException("Regions id, initial values and solution must be square grids matching size");


    this.size = size;
    this.regionsId = regionsId;
    this.cells = new Cell[size][size];
    this.cellsSolved = new Cell[size][size];
    this.difficulty = difficulty;

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        int value = initialValues[row][col];
        CellState cellState = (value != 0) ? CellState.FIXED : CellState.EDITABLE;
        cells[row][col] = new Cell(row, col, value, cellState);

        int solvedValue = solved[row][col];
        cellsSolved[row][col] = new Cell(row, col, solvedValue, cellState);
      }
    }

    updateGameState();
  }

  public Board(Puzzle puzzle) {
    this(
        puzzle.getSize(),
        puzzle.getRegionsId(),
        puzzle.getInitialValues(),
        puzzle.getDifficulty(),
        puzzle.getSolution()
    );
  }

  public boolean getIsSolvedAutomatically() { return isSolvedAutomatically; }

  public Difficulty getDifficulty() { return difficulty; }

  public int getSize() { return size; }

  public GameState getState() { return state; }

  public int getRegionId(int row, int col) { return regionsId[row][col]; }

  public Cell getCell(int row, int col) { return cells[row][col]; }

  public int getSolutionValue(int row, int col) {
    if (!isInBounds(row, col)) throw new IllegalArgumentException("Cell is out of bounds");

    return cellsSolved[row][col].getValue();
  }

  public boolean setValue(int row, int col, int value) {
    if (state != GameState.PLAYING) return false;
    if (!isInBounds(row, col)) return false;
    if (!isMoveAllowed(row, col, value)) return false;

    Cell cell = getCell(row, col);
    cell.setValue(value);
    updateGameState();

    return true;
  }

  public boolean clearValue(int row, int col) {
    return setValue(row, col, 0);
  }

  public boolean isMoveAllowed(int row, int col, int value) {
    if (!isInBounds(row, col)) return false;
    if (value < 0 || value > size) return false;
    return !cells[row][col].isFixed();
  }

  public boolean isSolved() {
    if (!isUnique()) return false;

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (cells[row][col].getValue() == 0) return false;
      }
    }

    return true;
  }

  private void updateGameState() {
    if (isSolved()) state = GameState.SOLVED;
    else state = GameState.PLAYING;
  }

  private boolean existsInRow(int row, int col, int value) {
    for (int c = 0; c < size; c++) {
      if (c == col) continue;
      if (cells[row][c].getValue() == value) return true;
    }
    return false;
  }

  private boolean existsInCol(int row, int col, int value) {
    for (int r = 0; r < size; r++) {
      if (r == row) continue;
      if (cells[r][col].getValue() == value) return true;
    }
    return false;
  }

  private boolean existsInRegion(int row, int col, int value) {
    int id = regionsId[row][col];
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (r == row && c == col) continue;
        if (regionsId[r][c] == id && cells[r][c].getValue() == value) return true;
      }
    }
    return false;
  }

  private boolean isInBounds(int row, int col) {
    return row >= 0 && row < size && col >= 0 && col < size;
  }

  // checks whether board has unique values in rows, columns and regions to easily handle SOLVED state
  private boolean isUnique() {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        int value = cells[row][col].getValue();
        if (value == 0) continue;
        if (existsInRow(row, col, value) ||
            existsInCol(row, col, value) ||
            existsInRegion(row, col, value)
        ) return false;
      }
    }
    return true;
  }

  public void reset() {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (!cells[row][col].isFixed()) cells[row][col].setValue(0);
      }
    }

    updateGameState();
  }

  public void solve() {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        cells[row][col].setValue(cellsSolved[row][col].getValue());
      }
    }

    isSolvedAutomatically = true;
    updateGameState();
  }

  public List<String> showRowRemaining(int row) {
    List<String> remaining = new ArrayList<>();

    for (int i = 1; i <= size; i++) {
      remaining.add(String.valueOf(i));
    }

    for (int col = 0; col < size; col++) {
      remaining.remove(String.valueOf(cells[row][col].getValue()));
    }

    return remaining;
  }

  @Override
  public String toString() {
    boolean[][] verticalBorders = buildVerticalBorders();
    boolean[][] horizontalBorders = buildHorizontalBorders();

    StringBuilder sb = new StringBuilder();

    // header
    sb.append("    ");
    for (int col = 0; col < size; col++) {
      sb.append(" ").append(col + 1).append("  ");
    }
    sb.append("\n");

    for (int row = 0; row < size; row++) {
      // horizontal border line above row
      sb.append("   ").append(Ansi.YELLOW);
      for (int col = 0; col < size; col++) {
        sb.append(getIntersectionChar(horizontalBorders, verticalBorders, row, col));
        sb.append(horizontalBorders[row][col] ? "───" : "   ");
      }
      sb.append(getIntersectionChar(horizontalBorders, verticalBorders, row, size));
      sb.append("\n").append(Ansi.RESET);

      // row with values
      sb.append(" ").append(row + 1).append(" ");
      for (int col = 0; col < size; col++) {
        sb.append(Ansi.YELLOW).append(verticalBorders[row][col] ? "│" : " ").append(Ansi.RESET);
        int value = cells[row][col].getValue();
        sb.append(" ").append(value == 0 ? "·" : value).append(" ");
      }
      sb.append(Ansi.YELLOW).append(verticalBorders[row][size] ? "│" : " ").append(Ansi.RESET);
      sb.append("\n");
    }

    // bottom border
    sb.append(Ansi.YELLOW).append("   ");
    for (int col = 0; col < size; col++) {
      sb.append(getIntersectionChar(horizontalBorders, verticalBorders, size, col));
      sb.append(horizontalBorders[size][col] ? "───" : "   ");
    }
    sb.append(getIntersectionChar(horizontalBorders, verticalBorders, size, size));
    sb.append("\n");

    return sb.toString();
  }

  private boolean[][] buildVerticalBorders() {
    // [row][colBorder], colBorder = 0..size
    boolean[][] borders = new boolean[size][size + 1];

    for (int row = 0; row < size; row++) {
      for (int colBorder = 0; colBorder <= size; colBorder++) {
        if (colBorder == 0 || colBorder == size) {
          borders[row][colBorder] = true; // outer frame
        } else {
          borders[row][colBorder] = regionsId[row][colBorder - 1] != regionsId[row][colBorder];
        }
      }
    }

    return borders;
  }

  private boolean[][] buildHorizontalBorders() {
    // [rowBorder][col], rowBorder = 0..size
    boolean[][] borders = new boolean[size + 1][size];

    for (int rowBorder = 0; rowBorder <= size; rowBorder++) {
      for (int col = 0; col < size; col++) {
        if (rowBorder == 0 || rowBorder == size) {
          borders[rowBorder][col] = true; // outer frame
        } else {
          borders[rowBorder][col] = regionsId[rowBorder - 1][col] != regionsId[rowBorder][col];
        }
      }
    }

    return borders;
  }

  private char getIntersectionChar(boolean[][] horizontalBorders, boolean[][] verticalBorders, int rowBorder, int colBorder) {
    boolean up = rowBorder > 0 && verticalSegmentExists(verticalBorders, rowBorder - 1, colBorder);
    boolean down = rowBorder < size && verticalSegmentExists(verticalBorders, rowBorder, colBorder);
    boolean left = colBorder > 0 && horizontalSegmentExists(horizontalBorders, rowBorder, colBorder - 1);
    boolean right = colBorder < size && horizontalSegmentExists(horizontalBorders, rowBorder, colBorder);

    return boxChar(up, down, left, right);
  }

  private boolean verticalSegmentExists(boolean[][] verticalBorders, int row, int colBorder) {
    return verticalBorders[row][colBorder];
  }

  private boolean horizontalSegmentExists(boolean[][] horizontalBorders, int rowBorder, int col) {
    return horizontalBorders[rowBorder][col];
  }

  private boolean hasSquareShape(int[][] grid, int size) {
    if (grid.length != size) {
      return false;
    }

    for (int row = 0; row < size; row++) {
      if (grid[row] == null || grid[row].length != size) {
        return false;
      }
    }

    return true;
  }

  private char boxChar(boolean up, boolean down, boolean left, boolean right) {
    if (up && down && left && right) return '┼';
    if (up && down && left) return '┤';
    if (up && down && right) return '├';
    if (left && right && up) return '┴';
    if (left && right && down) return '┬';
    if (down && right) return '┌';
    if (down && left) return '┐';
    if (up && right) return '└';
    if (up && left) return '┘';
    if (left && right) return '─';
    if (up && down) return '│';
    if (up || down) return '│';
    if (left || right) return '─';
    return ' ';
  }
}
