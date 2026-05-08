package sk.tuke.gamestudio.game.jigsawsudoku.core;

public class PuzzleRepository {
  private final PuzzleGenerator generator = new PuzzleGenerator();

  public Puzzle getRandomPuzzle(int size, Difficulty difficulty) {
    return generator.generate(size, difficulty);
  }
}