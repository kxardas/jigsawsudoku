package sk.tuke.gamestudio.game.jigsawsudoku.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    int size = 5;

    int[][] regionsId = {
            {0,0,0,0,1},
            {2,0,2,1,1},
            {2,2,2,1,1},
            {3,3,3,4,4},
            {3,3,4,4,4}
    };

    int[][] initialValues = {
            {4,0,0,0,2},
            {1,0,0,0,3},
            {0,0,0,0,0},
            {3,0,0,0,5},
            {5,0,0,0,1}
    };

    int[][] solution = {
            {4,5,1,3,2},
            {1,2,4,5,3},
            {2,3,5,1,4},
            {3,1,2,4,5},
            {5,4,3,2,1}
    };

    Puzzle puzzle = new Puzzle(size, regionsId, initialValues, solution, Difficulty.EASY);
    board = new Board(puzzle);
  }

  @Test
  void shouldCreateBoardFromPuzzle() {
    assertEquals(5, board.getSize());
    assertEquals(GameState.PLAYING, board.getState());
    assertEquals(Difficulty.EASY, board.getDifficulty());
  }

  @Test
  void shouldNotAllowChangingFixedCell() {
    assertFalse(board.setValue(0, 0, 2));
    assertEquals(4, board.getCell(0, 0).getValue());
  }

  @Test
  void shouldAllowValidMove() {
    assertTrue(board.setValue(0, 1, 5));
    assertEquals(5, board.getCell(0, 1).getValue());
  }

  @Test
  void shouldRejectMoveThatBreaksRowRule() {
    assertFalse(board.setValue(0, 1, 4)); // 4 already in row 0
    assertEquals(0, board.getCell(0, 1).getValue());
  }

  @Test
  void shouldRejectMoveThatBreaksColumnRule() {
    assertFalse(board.setValue(2, 0, 4)); // 4 already in column 0
    assertEquals(0, board.getCell(2, 0).getValue());
  }

  @Test
  void shouldRejectMoveThatBreaksRegionRule() {
    assertFalse(board.setValue(1, 1, 4)); // region conflict
    assertEquals(0, board.getCell(1, 1).getValue());
  }

  @Test
  void shouldReportMoveAsNotAllowedWhenItBreaksRules() {
    assertFalse(board.isMoveAllowed(0, 1, 4));
    assertFalse(board.isMoveAllowed(2, 0, 4));
    assertFalse(board.isMoveAllowed(1, 1, 4));
  }

  @Test
  void shouldAllowClearingEditableCellThroughMoveValidation() {
    board.setValue(0, 1, 5);

    assertTrue(board.isMoveAllowed(0, 1, 0));
    assertTrue(board.clearValue(0, 1));
    assertEquals(0, board.getCell(0, 1).getValue());
  }

  @Test
  void shouldClearEditableCell() {
    board.setValue(0, 1, 5);
    assertTrue(board.clearValue(0, 1));
    assertEquals(0, board.getCell(0, 1).getValue());
  }

  @Test
  void shouldReturnRemainingValuesForRow() {
    assertEquals(List.of("1", "3", "5"), board.showRowRemaining(0));
  }

  @Test
  void shouldNotBeSolvedAtStart() {
    assertFalse(board.isSolved());
    assertEquals(GameState.PLAYING, board.getState());
  }

  @Test
  void shouldSolveBoardAutomatically() {
    board.solve();

    assertTrue(board.isSolved());
    assertTrue(board.getIsSolvedAutomatically());
    assertEquals(GameState.SOLVED, board.getState());
  }

  @Test
  void shouldResetEditableCellsAndGameStateAfterBoardWasSolved() {
    board.solve();

    board.reset();

    assertFalse(board.isSolved());
    assertEquals(GameState.PLAYING, board.getState());
    assertEquals(4, board.getCell(0, 0).getValue());
    assertEquals(0, board.getCell(0, 1).getValue());
    assertEquals(5, board.getSolutionValue(0, 1));
  }

  @Test
  void shouldRejectPuzzleDataThatDoesNotMatchBoardSize() {
    int[][] tooShort = {
            {0, 0},
            {0, 0}
    };

    assertThrows(
            IllegalArgumentException.class,
            () -> new Board(3, tooShort, tooShort, Difficulty.EASY, tooShort)
    );
  }

  @Test
  void shouldChangeStateToSolvedWhenBoardIsCompletedCorrectly() {
    board.setValue(0, 1, 5);
    board.setValue(0, 2, 1);
    board.setValue(0, 3, 3);

    board.setValue(1, 1, 2);
    board.setValue(1, 2, 4);
    board.setValue(1, 3, 5);

    board.setValue(2, 0, 2);
    board.setValue(2, 1, 3);
    board.setValue(2, 2, 5);
    board.setValue(2, 3, 1);
    board.setValue(2, 4, 4);

    board.setValue(3, 1, 1);
    board.setValue(3, 2, 2);
    board.setValue(3, 3, 4);

    board.setValue(4, 1, 4);
    board.setValue(4, 2, 3);
    board.setValue(4, 3, 2);

    assertTrue(board.isSolved());
    assertEquals(GameState.SOLVED, board.getState());
  }
}
