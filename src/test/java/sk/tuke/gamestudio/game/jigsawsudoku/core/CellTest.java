package sk.tuke.gamestudio.game.jigsawsudoku.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

  @Test
  void shouldReturnTrueForFixedCell() {
    Cell cell = new Cell(0, 0, 4, CellState.FIXED);
    assertTrue(cell.isFixed());
  }

  @Test
  void shouldChangeValue() {
    Cell cell = new Cell(1, 1, 0, CellState.EDITABLE);

    cell.setValue(3);

    assertEquals(3, cell.getValue());
  }
}