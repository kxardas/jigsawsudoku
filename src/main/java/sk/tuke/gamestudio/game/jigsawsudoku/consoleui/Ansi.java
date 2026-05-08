package sk.tuke.gamestudio.game.jigsawsudoku.consoleui;

public class Ansi {
  public static final String RESET = "\u001B[0m";

  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String PURPLE = "\u001B[35m";
  public static final String GRAY = "\u001B[90m";

  public static final String BOLD = "\u001B[1m";
  public static final String ITALIC = "\u001B[3m";

  public static final String ERROR = BOLD + ITALIC + RED;
  public static final String BOLD_GREEN = BOLD + GREEN;
  public static final String BOLD_PURPLE = BOLD + PURPLE;
  public static final String ITALIC_GREEN = ITALIC + GREEN;
}
