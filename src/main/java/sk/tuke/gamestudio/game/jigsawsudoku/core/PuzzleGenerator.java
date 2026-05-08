package sk.tuke.gamestudio.game.jigsawsudoku.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class PuzzleGenerator {
  private static final int EMPTY = 0;

  private static final int PUZZLE_ATTEMPTS = 80;
  private static final int REGION_ATTEMPTS = 800;
  private static final int REMOVAL_PASSES = 3;

  private static final int SOLVER_NODE_LIMIT = 1_000_000;
  private static final int UNIQUE_NODE_LIMIT = 750_000;

  private static final int[][] DIRECTIONS = {
          {1, 0}, {-1, 0}, {0, 1}, {0, -1}
  };

  private final Random random;

  public PuzzleGenerator() {
    this(new Random());
  }

  public PuzzleGenerator(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null.");
    }

    this.random = random;
  }

  public Puzzle generate(int size, Difficulty difficulty) {
    validate(size, difficulty);

    for (int attempt = 0; attempt < PUZZLE_ATTEMPTS; attempt++) {
      int[][] regionsId;

      try {
        regionsId = generateRegions(size);
      } catch (IllegalStateException e) {
        continue;
      }

      int[][] solution = new int[size][size];

      if (!solveOne(solution, regionsId, size)) {
        continue;
      }

      int[][] initialValues = createInitialValues(solution, regionsId, size, difficulty);

      if (hasUniqueSolution(initialValues, regionsId, size)) {
        return new Puzzle(size, regionsId, initialValues, solution, difficulty);
      }
    }

    throw new IllegalStateException(
            "Could not generate puzzle for size " + size + " and difficulty " + difficulty
    );
  }

  private void validate(int size, Difficulty difficulty) {
    if (difficulty == null) {
      throw new IllegalArgumentException("Difficulty cannot be null.");
    }

    if (size < 2 || size > 16) {
      throw new IllegalArgumentException("Supported puzzle size is 2..16.");
    }
  }

  private int[][] generateRegions(int size) {
    for (int attempt = 0; attempt < REGION_ATTEMPTS; attempt++) {
      int maxDeviation = Math.min(size - 1, Math.max(1, size / 4) + attempt / 250);

      int[][] regions = new int[size][size];

      for (int[] row : regions) {
        Arrays.fill(row, -1);
      }

      int[] counts = new int[size];
      List<Cell> allCells = new ArrayList<>();

      for (int r = 0; r < size; r++) {
        for (int c = 0; c < size; c++) {
          allCells.add(new Cell(r, c));
        }
      }

      Collections.shuffle(allCells, random);

      for (int regionId = 0; regionId < size; regionId++) {
        Cell seed = allCells.get(regionId);
        regions[seed.row][seed.col] = regionId;
        counts[regionId] = 1;
      }

      int remaining = size * size - size;
      boolean failed = false;

      while (remaining > 0) {
        List<RegionFrontier> options = collectExpandableRegions(
                regions,
                counts,
                size,
                maxDeviation
        );

        if (options.isEmpty()) {
          failed = true;
          break;
        }

        RegionFrontier selected = options.get(random.nextInt(options.size()));
        Cell cell = pickWeightedCell(selected.cells, selected.regionId);

        regions[cell.row][cell.col] = selected.regionId;
        counts[selected.regionId]++;
        remaining--;
      }

      if (!failed && validateRegions(regions, size)) {
        return regions;
      }
    }

    throw new IllegalStateException("Could not generate connected regions.");
  }

  private List<RegionFrontier> collectExpandableRegions(
          int[][] regions,
          int[] counts,
          int size,
          int maxDeviation
  ) {
    List<RegionFrontier> options = new ArrayList<>();
    int smallestRegionSize = Integer.MAX_VALUE;

    for (int regionId = 0; regionId < size; regionId++) {
      if (counts[regionId] >= size) {
        continue;
      }

      List<Cell> frontier = collectFrontier(regions, regionId, maxDeviation);

      if (frontier.isEmpty()) {
        continue;
      }

      if (counts[regionId] < smallestRegionSize) {
        options.clear();
        smallestRegionSize = counts[regionId];
      }

      if (counts[regionId] == smallestRegionSize) {
        options.add(new RegionFrontier(regionId, frontier));
      }
    }

    return options;
  }

  private List<Cell> collectFrontier(int[][] regions, int regionId, int maxDeviation) {
    int size = regions.length;
    boolean[][] seen = new boolean[size][size];
    List<Cell> frontier = new ArrayList<>();

    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (regions[r][c] != regionId) {
          continue;
        }

        for (int[] direction : DIRECTIONS) {
          int nr = r + direction[0];
          int nc = c + direction[1];

          if (!isInside(nr, nc, size)) {
            continue;
          }

          if (regions[nr][nc] != -1) {
            continue;
          }

//          if (Math.abs(nr - regionId) > maxDeviation) {
//            continue;
//          }

          if (!seen[nr][nc]) {
            seen[nr][nc] = true;
            frontier.add(new Cell(nr, nc));
          }
        }
      }
    }

    return frontier;
  }

  private Cell pickWeightedCell(List<Cell> cells, int homeRow) {
    int totalWeight = 0;
    int[] weights = new int[cells.size()];

    for (int i = 0; i < cells.size(); i++) {
      Cell cell = cells.get(i);
      int distance = Math.abs(cell.row - homeRow);
      int weight = Math.max(1, 12 - distance * 4);

      weights[i] = weight;
      totalWeight += weight;
    }

    int roll = random.nextInt(totalWeight);

    for (int i = 0; i < cells.size(); i++) {
      roll -= weights[i];

      if (roll < 0) {
        return cells.get(i);
      }
    }

    return cells.get(cells.size() - 1);
  }

  private boolean validateRegions(int[][] regions, int size) {
    if (!hasSquareShape(regions, size)) {
      return false;
    }

    int[] counts = new int[size];

    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        int regionId = regions[r][c];

        if (regionId < 0 || regionId >= size) {
          return false;
        }

        counts[regionId]++;
      }
    }

    for (int count : counts) {
      if (count != size) {
        return false;
      }
    }

    for (int regionId = 0; regionId < size; regionId++) {
      if (!isRegionConnected(regions, regionId, size)) {
        return false;
      }
    }

    return true;
  }

  private boolean isRegionConnected(int[][] regions, int regionId, int size) {
    boolean[][] visited = new boolean[size][size];
    Queue<Cell> queue = new ArrayDeque<>();

    outer:
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (regions[r][c] == regionId) {
          queue.add(new Cell(r, c));
          visited[r][c] = true;
          break outer;
        }
      }
    }

    int visitedCount = 0;

    while (!queue.isEmpty()) {
      Cell cell = queue.remove();
      visitedCount++;

      for (int[] direction : DIRECTIONS) {
        int nr = cell.row + direction[0];
        int nc = cell.col + direction[1];

        if (!isInside(nr, nc, size)) {
          continue;
        }

        if (visited[nr][nc] || regions[nr][nc] != regionId) {
          continue;
        }

        visited[nr][nc] = true;
        queue.add(new Cell(nr, nc));
      }
    }

    return visitedCount == size;
  }

  private int[][] createInitialValues(
          int[][] solution,
          int[][] regionsId,
          int size,
          Difficulty difficulty
  ) {
    int targetGivens = targetGivenCount(size, difficulty);
    int[][] bestPuzzle = copyGrid(solution);
    int bestGivens = size * size;

    int removalPasses = size <= 9 ? REMOVAL_PASSES : 1;

    for (int pass = 0; pass < removalPasses; pass++) {
      int[][] puzzle = copyGrid(solution);
      List<Cell> cells = allCells(size);
      Collections.shuffle(cells, random);

      int givens = size * size;

      for (Cell cell : cells) {
        if (givens <= targetGivens) {
          break;
        }

        int backup = puzzle[cell.row][cell.col];
        puzzle[cell.row][cell.col] = EMPTY;

        CountResult result = countSolutions(
                puzzle,
                regionsId,
                size,
                2,
                UNIQUE_NODE_LIMIT
        );

        if (!result.aborted && result.solutions == 1) {
          givens--;
        } else {
          puzzle[cell.row][cell.col] = backup;
        }
      }

      if (givens < bestGivens) {
        bestGivens = givens;
        bestPuzzle = puzzle;
      }

      if (givens <= targetGivens) {
        return puzzle;
      }
    }

    return bestPuzzle;
  }

  private List<Cell> allCells(int size) {
    List<Cell> cells = new ArrayList<>();

    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        cells.add(new Cell(r, c));
      }
    }

    return cells;
  }

  private int targetGivenCount(int size, Difficulty difficulty) {
    int cells = size * size;
    double ratio;

    switch (difficulty) {
      case EASY:
        ratio = 0.34;
        break;
      case NORMAL:
        ratio = 0.28;
        break;
      case HARD:
        ratio = 0.22;
        break;
      default:
        ratio = 0.34;
        break;
    }

    return Math.max(size, (int) Math.round(cells * ratio));
  }

  private boolean solveOne(int[][] grid, int[][] regionsId, int size) {
    int[] rowMasks = new int[size];
    int[] columnMasks = new int[size];
    int[] regionMasks = new int[size];

    if (!loadMasks(grid, regionsId, size, rowMasks, columnMasks, regionMasks)) {
      return false;
    }

    Budget budget = new Budget(SOLVER_NODE_LIMIT);
    int allMask = allMask(size);

    return solveOneRecursive(
            grid,
            regionsId,
            size,
            rowMasks,
            columnMasks,
            regionMasks,
            allMask,
            budget
    );
  }

  private boolean solveOneRecursive(
          int[][] grid,
          int[][] regionsId,
          int size,
          int[] rowMasks,
          int[] columnMasks,
          int[] regionMasks,
          int allMask,
          Budget budget
  ) {
    if (!budget.touch()) {
      return false;
    }

    Choice choice = findBestCell(
            grid,
            regionsId,
            size,
            rowMasks,
            columnMasks,
            regionMasks,
            allMask
    );

    if (choice.invalid) {
      return false;
    }

    if (choice.complete()) {
      return true;
    }

    List<Integer> values = valuesFromMask(choice.mask);
    Collections.shuffle(values, random);

    int regionId = regionsId[choice.row][choice.col];

    for (int value : values) {
      int bit = bit(value);

      grid[choice.row][choice.col] = value;
      rowMasks[choice.row] |= bit;
      columnMasks[choice.col] |= bit;
      regionMasks[regionId] |= bit;

      if (solveOneRecursive(
              grid,
              regionsId,
              size,
              rowMasks,
              columnMasks,
              regionMasks,
              allMask,
              budget
      )) {
        return true;
      }

      grid[choice.row][choice.col] = EMPTY;
      rowMasks[choice.row] &= ~bit;
      columnMasks[choice.col] &= ~bit;
      regionMasks[regionId] &= ~bit;
    }

    return false;
  }

  private boolean hasUniqueSolution(int[][] grid, int[][] regionsId, int size) {
    CountResult result = countSolutions(grid, regionsId, size, 2, UNIQUE_NODE_LIMIT);
    return !result.aborted && result.solutions == 1;
  }

  private CountResult countSolutions(
          int[][] grid,
          int[][] regionsId,
          int size,
          int limit,
          int nodeLimit
  ) {
    int[] rowMasks = new int[size];
    int[] columnMasks = new int[size];
    int[] regionMasks = new int[size];

    CountResult result = new CountResult();

    if (!loadMasks(grid, regionsId, size, rowMasks, columnMasks, regionMasks)) {
      result.solutions = 0;
      return result;
    }

    Budget budget = new Budget(nodeLimit);
    int allMask = allMask(size);

    countSolutionsRecursive(
            grid,
            regionsId,
            size,
            rowMasks,
            columnMasks,
            regionMasks,
            allMask,
            result,
            limit,
            budget
    );

    result.aborted = budget.aborted;
    return result;
  }

  private void countSolutionsRecursive(
          int[][] grid,
          int[][] regionsId,
          int size,
          int[] rowMasks,
          int[] columnMasks,
          int[] regionMasks,
          int allMask,
          CountResult result,
          int limit,
          Budget budget
  ) {
    if (result.solutions >= limit) {
      return;
    }

    if (!budget.touch()) {
      return;
    }

    Choice choice = findBestCell(
            grid,
            regionsId,
            size,
            rowMasks,
            columnMasks,
            regionMasks,
            allMask
    );

    if (choice.invalid) {
      return;
    }

    if (choice.complete()) {
      result.solutions++;
      return;
    }

    List<Integer> values = valuesFromMask(choice.mask);
    int regionId = regionsId[choice.row][choice.col];

    for (int value : values) {
      int bit = bit(value);

      grid[choice.row][choice.col] = value;
      rowMasks[choice.row] |= bit;
      columnMasks[choice.col] |= bit;
      regionMasks[regionId] |= bit;

      countSolutionsRecursive(
              grid,
              regionsId,
              size,
              rowMasks,
              columnMasks,
              regionMasks,
              allMask,
              result,
              limit,
              budget
      );

      grid[choice.row][choice.col] = EMPTY;
      rowMasks[choice.row] &= ~bit;
      columnMasks[choice.col] &= ~bit;
      regionMasks[regionId] &= ~bit;

      if (result.solutions >= limit || budget.aborted) {
        return;
      }
    }
  }

  private Choice findBestCell(
          int[][] grid,
          int[][] regionsId,
          int size,
          int[] rowMasks,
          int[] columnMasks,
          int[] regionMasks,
          int allMask
  ) {
    Choice best = new Choice();
    int bestCount = Integer.MAX_VALUE;

    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (grid[r][c] != EMPTY) {
          continue;
        }

        int regionId = regionsId[r][c];

        if (regionId < 0 || regionId >= size) {
          best.invalid = true;
          return best;
        }

        int mask = allMask & ~(rowMasks[r] | columnMasks[c] | regionMasks[regionId]);
        int count = Integer.bitCount(mask);

        if (count == 0) {
          best.invalid = true;
          return best;
        }

        if (count < bestCount) {
          best.row = r;
          best.col = c;
          best.mask = mask;
          bestCount = count;

          if (count == 1) {
            return best;
          }
        }
      }
    }

    return best;
  }

  private boolean loadMasks(
          int[][] grid,
          int[][] regionsId,
          int size,
          int[] rowMasks,
          int[] columnMasks,
          int[] regionMasks
  ) {
    if (!hasSquareShape(grid, size) || !hasSquareShape(regionsId, size)) {
      return false;
    }

    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        int regionId = regionsId[r][c];

        if (regionId < 0 || regionId >= size) {
          return false;
        }

        int value = grid[r][c];

        if (value == EMPTY) {
          continue;
        }

        if (value < 1 || value > size) {
          return false;
        }

        int bit = bit(value);

        if ((rowMasks[r] & bit) != 0) {
          return false;
        }

        if ((columnMasks[c] & bit) != 0) {
          return false;
        }

        if ((regionMasks[regionId] & bit) != 0) {
          return false;
        }

        rowMasks[r] |= bit;
        columnMasks[c] |= bit;
        regionMasks[regionId] |= bit;
      }
    }

    return true;
  }

  private boolean hasSquareShape(int[][] grid, int size) {
    if (grid == null || grid.length != size) {
      return false;
    }

    for (int r = 0; r < size; r++) {
      if (grid[r] == null || grid[r].length != size) {
        return false;
      }
    }

    return true;
  }

  private List<Integer> valuesFromMask(int mask) {
    List<Integer> values = new ArrayList<>();

    while (mask != 0) {
      int bit = mask & -mask;
      values.add(Integer.numberOfTrailingZeros(bit) + 1);
      mask &= mask - 1;
    }

    return values;
  }

  private int allMask(int size) {
    return (1 << size) - 1;
  }

  private int bit(int value) {
    return 1 << (value - 1);
  }

  private boolean isInside(int row, int col, int size) {
    return row >= 0 && row < size && col >= 0 && col < size;
  }

  private int[][] copyGrid(int[][] source) {
    int[][] copy = new int[source.length][];

    for (int r = 0; r < source.length; r++) {
      copy[r] = Arrays.copyOf(source[r], source[r].length);
    }

    return copy;
  }

  private static class Cell {
    private final int row;
    private final int col;

    private Cell(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }

  private static class RegionFrontier {
    private final int regionId;
    private final List<Cell> cells;

    private RegionFrontier(int regionId, List<Cell> cells) {
      this.regionId = regionId;
      this.cells = cells;
    }
  }

  private static class Choice {
    private int row = -1;
    private int col = -1;
    private int mask = 0;
    private boolean invalid = false;

    private boolean complete() {
      return !invalid && row == -1;
    }
  }

  private static class CountResult {
    private int solutions = 0;
    private boolean aborted = false;
  }

  private static class Budget {
    private final int limit;
    private int nodes = 0;
    private boolean aborted = false;

    private Budget(int limit) {
      this.limit = limit;
    }

    private boolean touch() {
      nodes++;

      if (nodes > limit) {
        aborted = true;
        return false;
      }

      return true;
    }
  }
}
