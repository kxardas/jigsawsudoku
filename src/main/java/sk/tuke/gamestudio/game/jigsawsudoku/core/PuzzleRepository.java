package sk.tuke.gamestudio.game.jigsawsudoku.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleRepository {
  private final List<PuzzleTemplate> puzzles = new ArrayList<>();
  private final Random random = new Random();

  public PuzzleRepository() {
    loadPuzzles();
  }

  public Puzzle getRandomPuzzle(int size, Difficulty difficulty) {
    List<PuzzleTemplate> filtered = new ArrayList<>();

    for (PuzzleTemplate puzzle : puzzles) {
      if (puzzle.size == size && puzzle.difficulty == difficulty) {
        filtered.add(puzzle);
      }
    }

    if (filtered.isEmpty()) {
      throw new IllegalStateException("No puzzles for size " + size + " and difficulty: " + difficulty);
    }

    PuzzleTemplate selected = filtered.get(random.nextInt(filtered.size()));
    return createRandomlyRotatedPuzzle(selected);
  }

  private Puzzle createRandomlyRotatedPuzzle(PuzzleTemplate template) {
    int turns = random.nextInt(4); // 0, 1, 2, or 3 clockwise 90-degree turns.

    return new Puzzle(
            template.size,
            rotateMatrix(template.regionsId, turns),
            rotateMatrix(template.initialValues, turns),
            rotateMatrix(template.solution, turns),
            template.difficulty
    );
  }

  private static int[][] rotateMatrix(int[][] matrix, int turns) {
    int normalizedTurns = ((turns % 4) + 4) % 4;
    int[][] result = copyMatrix(matrix);

    for (int i = 0; i < normalizedTurns; i++) {
      result = rotateClockwiseOnce(result);
    }

    return result;
  }

  private static int[][] rotateClockwiseOnce(int[][] matrix) {
    int size = matrix.length;
    int[][] rotated = new int[size][size];

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        rotated[col][size - 1 - row] = matrix[row][col];
      }
    }

    return rotated;
  }

  private void loadPuzzles() {
    load5x5Puzzles();
    load7x7Puzzles();
    load9x9Puzzles();
  }

  private void load5x5Puzzles() {
    add(5, Difficulty.EASY,
            new int[][]{
                    {0, 0, 0, 0, 1},
                    {2, 0, 2, 1, 1},
                    {2, 2, 2, 1, 1},
                    {3, 3, 3, 4, 4},
                    {3, 3, 4, 4, 4},
            },
            new int[][]{
                    {4, 0, 0, 0, 2},
                    {1, 0, 0, 0, 3},
                    {0, 0, 0, 0, 0},
                    {3, 0, 0, 0, 5},
                    {5, 0, 0, 0, 1},
            },
            new int[][]{
                    {4, 5, 1, 3, 2},
                    {1, 2, 4, 5, 3},
                    {2, 3, 5, 1, 4},
                    {3, 1, 2, 4, 5},
                    {5, 4, 3, 2, 1},
            });

    add(5, Difficulty.EASY,
            new int[][]{
                    {0, 0, 1, 1, 1},
                    {0, 2, 2, 2, 1},
                    {0, 3, 2, 4, 1},
                    {0, 3, 2, 4, 4},
                    {3, 3, 3, 4, 4},
            },
            new int[][]{
                    {0, 0, 3, 5, 0},
                    {0, 0, 0, 0, 1},
                    {0, 0, 0, 0, 0},
                    {4, 0, 0, 0, 0},
                    {0, 5, 2, 0, 0},
            },
            new int[][]{
                    {2, 1, 3, 5, 4},
                    {5, 2, 4, 3, 1},
                    {3, 4, 5, 1, 2},
                    {4, 3, 1, 2, 5},
                    {1, 5, 2, 4, 3},
            });

    add(5, Difficulty.EASY,
            new int[][]{
                    {0, 1, 2, 2, 2},
                    {0, 1, 1, 2, 2},
                    {0, 1, 3, 4, 4},
                    {0, 1, 3, 3, 4},
                    {0, 3, 3, 4, 4},
            },
            new int[][]{
                    {0, 0, 4, 0, 0},
                    {0, 0, 5, 0, 2},
                    {0, 4, 0, 2, 0},
                    {3, 0, 2, 0, 0},
                    {0, 0, 1, 0, 0},
            },
            new int[][]{
                    {1, 2, 4, 5, 3},
                    {4, 3, 5, 1, 2},
                    {5, 4, 3, 2, 1},
                    {3, 1, 2, 4, 5},
                    {2, 5, 1, 3, 4},
            });

    add(5, Difficulty.NORMAL,
            new int[][]{
                    {0, 1, 1, 1, 2},
                    {0, 1, 2, 2, 2},
                    {0, 1, 3, 4, 2},
                    {0, 0, 3, 4, 4},
                    {3, 3, 3, 4, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 2},
                    {3, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1},
                    {4, 0, 0, 0, 0},
            },
            new int[][]{
                    {5, 3, 4, 1, 2},
                    {3, 2, 1, 5, 4},
                    {1, 5, 2, 4, 3},
                    {2, 4, 5, 3, 1},
                    {4, 1, 3, 2, 5},
            });

    add(5, Difficulty.NORMAL,
            new int[][]{
                    {0, 0, 0, 0, 1},
                    {0, 2, 2, 1, 1},
                    {2, 2, 2, 1, 1},
                    {3, 3, 4, 4, 4},
                    {3, 3, 3, 4, 4},
            },
            new int[][]{
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 1, 2},
                    {0, 0, 0, 0, 0},
                    {4, 5, 0, 0, 0},
                    {0, 0, 2, 0, 0},
            },
            new int[][]{
                    {2, 4, 1, 3, 5},
                    {5, 3, 4, 1, 2},
                    {1, 2, 5, 4, 3},
                    {4, 5, 3, 2, 1},
                    {3, 1, 2, 5, 4},
            });

    add(5, Difficulty.NORMAL,
            new int[][]{
                    {0, 0, 0, 1, 1},
                    {0, 2, 2, 2, 1},
                    {0, 2, 2, 1, 1},
                    {3, 3, 4, 4, 4},
                    {3, 3, 3, 4, 4},
            },
            new int[][]{
                    {4, 0, 0, 0, 0},
                    {3, 1, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 1},
                    {0, 0, 0, 0, 2},
            },
            new int[][]{
                    {4, 2, 5, 1, 3},
                    {3, 1, 2, 4, 5},
                    {1, 5, 3, 2, 4},
                    {2, 3, 4, 5, 1},
                    {5, 4, 1, 3, 2},
            });

    add(5, Difficulty.HARD,
            new int[][]{
                    {0, 1, 1, 1, 1},
                    {0, 2, 2, 3, 1},
                    {0, 0, 2, 3, 3},
                    {0, 2, 2, 3, 3},
                    {4, 4, 4, 4, 4},
            },
            new int[][]{
                    {0, 2, 0, 0, 0},
                    {0, 0, 0, 5, 0},
                    {2, 0, 0, 0, 1},
                    {0, 4, 0, 0, 0},
                    {0, 0, 0, 2, 0},
            },
            new int[][]{
                    {3, 2, 4, 1, 5},
                    {4, 1, 2, 5, 3},
                    {2, 5, 3, 4, 1},
                    {1, 4, 5, 3, 2},
                    {5, 3, 1, 2, 4},
            });

    add(5, Difficulty.HARD,
            new int[][]{
                    {0, 1, 1, 1, 1},
                    {0, 1, 2, 2, 2},
                    {0, 0, 2, 3, 2},
                    {0, 3, 3, 3, 3},
                    {4, 4, 4, 4, 4},
            },
            new int[][]{
                    {1, 0, 0, 0, 0},
                    {0, 0, 2, 0, 0},
                    {0, 2, 0, 3, 0},
                    {0, 0, 4, 0, 0},
                    {0, 0, 0, 0, 5},
            },
            new int[][]{
                    {1, 3, 5, 4, 2},
                    {4, 1, 2, 5, 3},
                    {5, 2, 1, 3, 4},
                    {3, 5, 4, 2, 1},
                    {2, 4, 3, 1, 5},
            });

    add(5, Difficulty.HARD,
            new int[][]{
                    {0, 0, 0, 1, 2},
                    {0, 0, 1, 1, 2},
                    {3, 1, 1, 2, 2},
                    {3, 3, 4, 4, 2},
                    {3, 3, 4, 4, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 0},
                    {1, 4, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 5},
                    {0, 0, 0, 0, 0},
            },
            new int[][]{
                    {3, 5, 2, 1, 4},
                    {1, 4, 3, 5, 2},
                    {5, 2, 4, 3, 1},
                    {4, 3, 1, 2, 5},
                    {2, 1, 5, 4, 3},
            });
  }

  private void load7x7Puzzles() {
    add(7, Difficulty.EASY,
            new int[][]{
                    {0, 0, 1, 1, 1, 2, 2},
                    {0, 0, 1, 3, 3, 3, 2},
                    {0, 0, 1, 3, 2, 2, 2},
                    {0, 4, 1, 3, 5, 5, 2},
                    {4, 4, 1, 3, 6, 5, 5},
                    {4, 4, 6, 3, 6, 6, 5},
                    {4, 4, 6, 6, 6, 5, 5},
            },
            new int[][]{
                    {0, 7, 0, 0, 0, 0, 0},
                    {0, 2, 4, 0, 0, 0, 5},
                    {0, 0, 5, 0, 0, 0, 0},
                    {0, 0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 0, 4, 0, 0},
                    {6, 0, 0, 0, 7, 1, 0},
                    {0, 0, 0, 0, 0, 2, 0},
            },
            new int[][]{
                    {5, 7, 1, 3, 2, 4, 6},
                    {1, 2, 4, 7, 3, 6, 5},
                    {4, 6, 5, 2, 1, 3, 7},
                    {3, 4, 7, 1, 6, 5, 2},
                    {2, 3, 6, 5, 4, 7, 1},
                    {6, 5, 2, 4, 7, 1, 3},
                    {7, 1, 3, 6, 5, 2, 4},
            });

    add(7, Difficulty.EASY,
            new int[][]{
                    {0, 0, 0, 1, 2, 2, 2},
                    {0, 1, 1, 1, 2, 2, 2},
                    {0, 3, 3, 1, 1, 1, 2},
                    {0, 3, 3, 4, 4, 4, 4},
                    {0, 3, 5, 5, 5, 5, 4},
                    {3, 3, 5, 6, 5, 5, 4},
                    {6, 6, 6, 6, 6, 6, 4},
            },
            new int[][]{
                    {0, 0, 2, 0, 0, 4, 0},
                    {0, 4, 0, 0, 0, 0, 0},
                    {4, 0, 0, 0, 5, 2, 0},
                    {6, 0, 0, 0, 0, 0, 5},
                    {0, 7, 3, 0, 0, 0, 4},
                    {0, 0, 0, 0, 0, 1, 0},
                    {0, 1, 0, 0, 7, 0, 0},
            },
            new int[][]{
                    {3, 5, 2, 7, 6, 4, 1},
                    {7, 4, 6, 1, 3, 5, 2},
                    {4, 6, 1, 3, 5, 2, 7},
                    {6, 3, 4, 2, 1, 7, 5},
                    {1, 7, 3, 5, 2, 6, 4},
                    {5, 2, 7, 6, 4, 1, 3},
                    {2, 1, 5, 4, 7, 3, 6},
            });

    add(7, Difficulty.EASY,
            new int[][]{
                    {0, 0, 0, 0, 1, 1, 2},
                    {0, 1, 1, 1, 1, 3, 2},
                    {0, 1, 3, 3, 3, 3, 2},
                    {0, 4, 3, 2, 2, 2, 2},
                    {4, 4, 3, 5, 5, 5, 5},
                    {4, 6, 6, 6, 5, 6, 5},
                    {4, 4, 4, 6, 6, 6, 5},
            },
            new int[][]{
                    {0, 3, 0, 2, 0, 0, 0},
                    {5, 0, 0, 1, 2, 0, 0},
                    {0, 0, 0, 0, 4, 0, 0},
                    {4, 0, 0, 0, 0, 0, 6},
                    {0, 0, 1, 0, 0, 0, 0},
                    {0, 0, 4, 7, 0, 0, 5},
                    {0, 0, 0, 5, 0, 1, 0},
            },
            new int[][]{
                    {6, 3, 7, 2, 5, 4, 1},
                    {5, 6, 3, 1, 2, 7, 4},
                    {1, 7, 5, 6, 4, 3, 2},
                    {4, 1, 2, 3, 7, 5, 6},
                    {7, 5, 1, 4, 6, 2, 3},
                    {3, 2, 4, 7, 1, 6, 5},
                    {2, 4, 6, 5, 3, 1, 7},
            });

    add(7, Difficulty.NORMAL,
            new int[][]{
                    {0, 0, 0, 1, 1, 1, 1},
                    {0, 0, 0, 0, 1, 1, 1},
                    {2, 2, 3, 3, 3, 4, 4},
                    {2, 2, 3, 5, 5, 5, 4},
                    {2, 2, 3, 5, 5, 5, 4},
                    {6, 2, 3, 3, 5, 4, 4},
                    {6, 6, 6, 6, 6, 6, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 1, 0},
                    {7, 5, 1, 6, 0, 0, 3},
                    {6, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 2},
                    {4, 0, 0, 3, 2, 5, 6},
                    {0, 6, 0, 0, 0, 0, 0},
            },
            new int[][]{
                    {2, 3, 4, 7, 6, 1, 5},
                    {7, 5, 1, 6, 4, 2, 3},
                    {6, 2, 5, 4, 1, 3, 7},
                    {3, 4, 2, 5, 7, 6, 1},
                    {5, 7, 6, 1, 3, 4, 2},
                    {4, 1, 7, 3, 2, 5, 6},
                    {1, 6, 3, 2, 5, 7, 4},
            });

    add(7, Difficulty.NORMAL,
            new int[][]{
                    {0, 0, 1, 2, 2, 2, 2},
                    {0, 1, 1, 1, 1, 2, 2},
                    {0, 0, 1, 3, 3, 3, 2},
                    {0, 0, 1, 4, 3, 3, 3},
                    {4, 4, 4, 4, 5, 5, 3},
                    {6, 4, 6, 4, 5, 5, 5},
                    {6, 6, 6, 6, 6, 5, 5},
            },
            new int[][]{
                    {0, 7, 0, 0, 0, 3, 0},
                    {0, 0, 0, 0, 0, 0, 5},
                    {0, 6, 0, 0, 1, 5, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 5, 3, 0, 0, 1, 0},
                    {3, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 2, 0},
            },
            new int[][]{
                    {4, 7, 5, 6, 2, 3, 1},
                    {1, 2, 6, 4, 3, 7, 5},
                    {2, 6, 7, 3, 1, 5, 4},
                    {5, 3, 1, 2, 7, 4, 6},
                    {6, 5, 3, 7, 4, 1, 2},
                    {3, 4, 2, 1, 5, 6, 7},
                    {7, 1, 4, 5, 6, 2, 3},
            });

    add(7, Difficulty.NORMAL,
            new int[][]{
                    {0, 0, 0, 0, 1, 1, 1},
                    {0, 0, 2, 2, 1, 1, 1},
                    {0, 2, 2, 2, 3, 1, 4},
                    {5, 2, 2, 3, 3, 4, 4},
                    {5, 5, 3, 3, 6, 4, 4},
                    {5, 5, 3, 6, 6, 6, 4},
                    {5, 5, 3, 6, 6, 6, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 3, 0, 4},
                    {0, 0, 4, 0, 0, 7, 0},
                    {2, 0, 0, 0, 0, 0, 6},
                    {0, 5, 0, 0, 7, 0, 0},
                    {3, 0, 2, 4, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
            },
            new int[][]{
                    {7, 4, 3, 2, 6, 1, 5},
                    {6, 1, 7, 5, 3, 2, 4},
                    {5, 2, 4, 6, 1, 7, 3},
                    {2, 3, 1, 7, 4, 5, 6},
                    {1, 5, 6, 3, 7, 4, 2},
                    {3, 7, 2, 4, 5, 6, 1},
                    {4, 6, 5, 1, 2, 3, 7},
            });

    add(7, Difficulty.HARD,
            new int[][]{
                    {0, 0, 0, 1, 1, 1, 1},
                    {0, 0, 0, 0, 1, 1, 1},
                    {2, 2, 3, 3, 3, 4, 4},
                    {2, 2, 3, 5, 5, 5, 4},
                    {2, 2, 3, 5, 5, 5, 4},
                    {6, 2, 3, 3, 5, 4, 4},
                    {6, 6, 6, 6, 6, 6, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 1, 0},
                    {7, 5, 0, 6, 0, 0, 0},
                    {6, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 2},
                    {4, 0, 0, 3, 2, 5, 6},
                    {0, 0, 0, 0, 0, 0, 0},
            },
            new int[][]{
                    {2, 3, 4, 7, 6, 1, 5},
                    {7, 5, 1, 6, 4, 2, 3},
                    {6, 2, 5, 4, 1, 3, 7},
                    {3, 4, 2, 5, 7, 6, 1},
                    {5, 7, 6, 1, 3, 4, 2},
                    {4, 1, 7, 3, 2, 5, 6},
                    {1, 6, 3, 2, 5, 7, 4},
            });

    add(7, Difficulty.HARD,
            new int[][]{
                    {0, 0, 1, 2, 2, 2, 2},
                    {0, 1, 1, 1, 1, 2, 2},
                    {0, 0, 1, 3, 3, 3, 2},
                    {0, 0, 1, 4, 3, 3, 3},
                    {4, 4, 4, 4, 5, 5, 3},
                    {6, 4, 6, 4, 5, 5, 5},
                    {6, 6, 6, 6, 6, 5, 5},
            },
            new int[][]{
                    {0, 7, 0, 0, 0, 3, 0},
                    {0, 0, 0, 0, 0, 0, 5},
                    {0, 6, 0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 5, 0, 0, 0, 1, 0},
                    {3, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 2, 0},
            },
            new int[][]{
                    {4, 7, 5, 6, 2, 3, 1},
                    {1, 2, 6, 4, 3, 7, 5},
                    {2, 6, 7, 3, 1, 5, 4},
                    {5, 3, 1, 2, 7, 4, 6},
                    {6, 5, 3, 7, 4, 1, 2},
                    {3, 4, 2, 1, 5, 6, 7},
                    {7, 1, 4, 5, 6, 2, 3},
            });

    add(7, Difficulty.HARD,
            new int[][]{
                    {0, 0, 0, 0, 1, 1, 1},
                    {0, 0, 2, 2, 1, 1, 1},
                    {0, 2, 2, 2, 3, 1, 4},
                    {5, 2, 2, 3, 3, 4, 4},
                    {5, 5, 3, 3, 6, 4, 4},
                    {5, 5, 3, 6, 6, 6, 4},
                    {5, 5, 3, 6, 6, 6, 4},
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 3, 0, 4},
                    {0, 0, 4, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 6},
                    {0, 5, 0, 0, 7, 0, 0},
                    {3, 0, 2, 4, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
            },
            new int[][]{
                    {7, 4, 3, 2, 6, 1, 5},
                    {6, 1, 7, 5, 3, 2, 4},
                    {5, 2, 4, 6, 1, 7, 3},
                    {2, 3, 1, 7, 4, 5, 6},
                    {1, 5, 6, 3, 7, 4, 2},
                    {3, 7, 2, 4, 5, 6, 1},
                    {4, 6, 5, 1, 2, 3, 7},
            });
  }

  private void load9x9Puzzles() {
    int[][] regionsId = new int[][]{
            {0, 0, 0, 0, 1, 1, 2, 2, 2},
            {0, 3, 3, 3, 3, 1, 1, 2, 2},
            {0, 0, 3, 3, 3, 3, 1, 2, 2},
            {0, 0, 4, 4, 3, 4, 1, 2, 2},
            {5, 5, 5, 4, 4, 4, 1, 1, 1},
            {5, 5, 5, 4, 6, 6, 7, 7, 7},
            {5, 5, 8, 4, 4, 6, 6, 6, 7},
            {5, 8, 8, 8, 8, 6, 7, 7, 7},
            {8, 8, 8, 8, 6, 6, 6, 7, 7},
    };

    int[][] solutionA = new int[][]{
            {5, 1, 8, 6, 9, 4, 3, 2, 7},
            {7, 9, 3, 8, 1, 6, 2, 5, 4},
            {4, 3, 6, 5, 2, 7, 8, 1, 9},
            {9, 2, 1, 3, 4, 5, 7, 6, 8},
            {2, 8, 4, 7, 6, 9, 5, 3, 1},
            {3, 7, 9, 4, 5, 2, 1, 8, 6},
            {6, 5, 7, 2, 8, 1, 9, 4, 3},
            {1, 6, 5, 9, 3, 8, 4, 7, 2},
            {8, 4, 2, 1, 7, 3, 6, 9, 5},
    };

    int[][] solutionB = permuteDigits(solutionA, 2, 3, 4, 5, 6, 7, 8, 9, 1);
    int[][] solutionC = permuteDigits(solutionA, 9, 8, 7, 6, 5, 4, 3, 2, 1);

    add(9, Difficulty.EASY, regionsId, givens(solutionA,
            "101101101",
            "011011111",
            "110110010",
            "001110001",
            "111001100",
            "000010101",
            "011100101",
            "010011011",
            "100000101"
    ), solutionA);

    add(9, Difficulty.EASY, regionsId, givens(solutionB,
            "010111010",
            "111011111",
            "110111011",
            "011110101",
            "111111100",
            "000101111",
            "111010101",
            "011001111",
            "101100111"
    ), solutionB);

    add(9, Difficulty.EASY, regionsId, givens(solutionC,
            "111010010",
            "000111111",
            "111010110",
            "011101001",
            "111011111",
            "001000101",
            "011110101",
            "110001111",
            "010100101"
    ), solutionC);

    String[] normalMask = new String[]{
            "000000000",
            "000011111",
            "110010000",
            "001100001",
            "111001100",
            "000000001",
            "011000101",
            "010000011",
            "000000101"
    };

    add(9, Difficulty.NORMAL, regionsId, givens(solutionA, normalMask), solutionA);
    add(9, Difficulty.NORMAL, regionsId, givens(solutionB, normalMask), solutionB);
    add(9, Difficulty.NORMAL, regionsId, givens(solutionC, normalMask), solutionC);

    String[] hardMask = new String[]{
            "000000000",
            "000011110",
            "100000000",
            "001100001",
            "011000100",
            "000000001",
            "011000000",
            "010000001",
            "000000000"
    };

    add(9, Difficulty.HARD, regionsId, givens(solutionA, hardMask), solutionA);
    add(9, Difficulty.HARD, regionsId, givens(solutionB, hardMask), solutionB);
    add(9, Difficulty.HARD, regionsId, givens(solutionC, hardMask), solutionC);
  }

  private void add(int size, Difficulty difficulty, int[][] regionsId, int[][] initialValues, int[][] solution) {
    validatePuzzle(size, regionsId, initialValues, solution);
    puzzles.add(new PuzzleTemplate(size, difficulty, regionsId, initialValues, solution));
  }

  private static int[][] givens(int[][] solution, String... mask) {
    int size = solution.length;

    if (mask.length != size) {
      throw new IllegalArgumentException("Given mask row count must be " + size);
    }

    int[][] initialValues = new int[size][size];

    for (int row = 0; row < size; row++) {
      if (mask[row].length() != size) {
        throw new IllegalArgumentException("Given mask row " + row + " must have length " + size);
      }

      for (int col = 0; col < size; col++) {
        initialValues[row][col] = mask[row].charAt(col) == '1' ? solution[row][col] : 0;
      }
    }

    return initialValues;
  }

  private static int[][] permuteDigits(int[][] source, int... mapping) {
    int size = source.length;

    if (mapping.length != size) {
      throw new IllegalArgumentException("Digit mapping must contain exactly " + size + " values");
    }

    int[][] result = new int[size][size];

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        int value = source[row][col];
        result[row][col] = mapping[value - 1];
      }
    }

    return result;
  }

  private static void validatePuzzle(int size, int[][] regionsId, int[][] initialValues, int[][] solution) {
    validateSquareMatrix("regionsId", regionsId, size);
    validateSquareMatrix("initialValues", initialValues, size);
    validateSquareMatrix("solution", solution, size);

    int[] regionCounts = new int[size];

    for (int row = 0; row < size; row++) {
      boolean[] rowValues = new boolean[size + 1];

      for (int col = 0; col < size; col++) {
        int value = solution[row][col];
        if (value < 1 || value > size || rowValues[value]) {
          throw new IllegalArgumentException("Bad solution row " + row);
        }
        rowValues[value] = true;

        int regionId = regionsId[row][col];
        if (regionId < 0 || regionId >= size) {
          throw new IllegalArgumentException("Bad region id at [" + row + "][" + col + "]: " + regionId);
        }
        regionCounts[regionId]++;

        int initialValue = initialValues[row][col];
        if (initialValue < 0 || initialValue > size) {
          throw new IllegalArgumentException("Bad initial value at [" + row + "][" + col + "]: " + initialValue);
        }
        if (initialValue != 0 && initialValue != value) {
          throw new IllegalArgumentException("Initial value does not match solution at [" + row + "][" + col + "]");
        }
      }
    }

    for (int col = 0; col < size; col++) {
      boolean[] colValues = new boolean[size + 1];

      for (int row = 0; row < size; row++) {
        int value = solution[row][col];
        if (colValues[value]) {
          throw new IllegalArgumentException("Bad solution column " + col);
        }
        colValues[value] = true;
      }
    }

    for (int regionId = 0; regionId < size; regionId++) {
      if (regionCounts[regionId] != size) {
        throw new IllegalArgumentException("Region " + regionId + " has " + regionCounts[regionId] + " cells, expected " + size);
      }

      boolean[] regionValues = new boolean[size + 1];

      for (int row = 0; row < size; row++) {
        for (int col = 0; col < size; col++) {
          if (regionsId[row][col] == regionId) {
            int value = solution[row][col];
            if (regionValues[value]) {
              throw new IllegalArgumentException("Bad solution region " + regionId);
            }
            regionValues[value] = true;
          }
        }
      }
    }
  }

  private static void validateSquareMatrix(String name, int[][] matrix, int size) {
    if (matrix == null || matrix.length != size) {
      throw new IllegalArgumentException(name + " must have " + size + " rows");
    }

    for (int row = 0; row < size; row++) {
      if (matrix[row] == null || matrix[row].length != size) {
        throw new IllegalArgumentException(name + " row " + row + " must have " + size + " columns");
      }
    }
  }

  private static int[][] copyMatrix(int[][] matrix) {
    int[][] copy = new int[matrix.length][matrix.length];

    for (int row = 0; row < matrix.length; row++) {
      System.arraycopy(matrix[row], 0, copy[row], 0, matrix.length);
    }

    return copy;
  }

  private static final class PuzzleTemplate {
    private final int size;
    private final Difficulty difficulty;
    private final int[][] regionsId;
    private final int[][] initialValues;
    private final int[][] solution;

    private PuzzleTemplate(int size, Difficulty difficulty, int[][] regionsId, int[][] initialValues, int[][] solution) {
      this.size = size;
      this.difficulty = difficulty;
      this.regionsId = copyMatrix(regionsId);
      this.initialValues = copyMatrix(initialValues);
      this.solution = copyMatrix(solution);
    }
  }
}
