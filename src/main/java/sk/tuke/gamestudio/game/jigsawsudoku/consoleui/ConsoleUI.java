package sk.tuke.gamestudio.game.jigsawsudoku.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.jigsawsudoku.core.*;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
  protected Board board;
  private final Scanner scanner = new Scanner(System.in);

  @Autowired
  private ScoreService scoreService;

  @Autowired
  private CommentService commentService;

  @Autowired
  private RatingService ratingService;


  private final PuzzleRepository puzzleRepository = new PuzzleRepository();

  private long fullSeconds = 0;

  private Difficulty difficulty;

  public void showMenu() {
    boolean isWrong = false;
    while (true) {

      printMenu();
      if (isWrong) System.out.println(Ansi.ERROR + "Wrong choice." + Ansi.RESET);
      System.out.print(Ansi.BOLD_PURPLE + "Your choice -> " + Ansi.RESET);
      String line = scanner.nextLine().trim();

      if (line.isEmpty()) {
        isWrong = true;
        continue;
      }

      char input = line.charAt(0);
      isWrong = false;

      switch (input) {
        case '1':
          chooseMode();
          if (board != null) play();
          break;
        case '2':
          comments();
          break;
        case '3':
          ratings();
          break;
        case '4':
          topScores();
          break;
        case '5':
          return;
        default:
          isWrong = true;
          break;
      }
    }
  }

  public void play() {
    long startTime = System.currentTimeMillis();
    boolean isBack;

    printGameIntro();

    do {
      show();
      isBack = !handleInput();

      if (isBack) break;
    } while(board.getState() != GameState.SOLVED);

    if (isBack) return;

    show();

    if (board.getIsSolvedAutomatically()) {
      System.out.println(Ansi.BOLD_GREEN + "Solved automatically." + Ansi.RESET);
      System.out.print("Press Enter to continue...");
      scanner.nextLine();
      return;
    }

    long elapsedTime = System.currentTimeMillis() - startTime;
    long seconds = elapsedTime / 1000;
    long minutes = seconds / 60;
    seconds %= 60;
    fullSeconds = elapsedTime / 1000;

    System.out.println("Congratulations! You won!!!\nElapsed Time: " + formatDuration(minutes, seconds));
    board = null;
    saveScore();
  }

  public void printMenu() {
    System.out.println();
    System.out.println(Ansi.PURPLE + "╔══════════════════════════╗" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "║ " + Ansi.GREEN + "Welcome to" + Ansi.BOLD + Ansi.RED + " Jigsaw Sudoku" + Ansi.RESET + Ansi.PURPLE + " ║" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "╚══════════════════════════╝" + Ansi.RESET);
    System.out.println(Ansi.ITALIC + Ansi.GRAY + "1. " + Ansi.RESET + "Play Sudoku");
    System.out.println(Ansi.ITALIC + Ansi.GRAY + "2. " + Ansi.RESET + "Comments");
    System.out.println(Ansi.ITALIC + Ansi.GRAY + "3. " + Ansi.RESET + "Rates");
    System.out.println(Ansi.ITALIC + Ansi.GRAY + "4. " + Ansi.RESET + "Top scores");
    System.out.println(Ansi.ITALIC + Ansi.GRAY + "5. " + Ansi.RESET + "Quit");
  }

  private void printGameIntro() {
    System.out.println();
    System.out.print(Ansi.BOLD + "Jigsaw Sudoku ");
    switch (formatDifficulty(difficulty)) {
      case "Easy" -> System.out.println(Ansi.GREEN + "Easy");
      case "Normal" -> System.out.println(Ansi.YELLOW + "Normal");
      case "Hard" -> System.out.println(Ansi.RED + "Hard");
    }

    System.out.println(Ansi.RESET + Ansi.GREEN + "Enter move as:" + Ansi.BOLD_PURPLE + " row col value" + Ansi.RESET);
    System.out.println(Ansi.GREEN + "Example:" + Ansi.BOLD_PURPLE + " 1 2 5" + Ansi.RESET);
    System.out.println(Ansi.GREEN + "Use value" + Ansi.BOLD_PURPLE + " 0 " + Ansi.RESET + Ansi.GREEN + "to clear a cell.");
    System.out.println(Ansi.GREEN + "Type " + Ansi.BOLD_PURPLE + "'back'" + Ansi.RESET + Ansi.GREEN + " or " + Ansi.BOLD_PURPLE +"'b'" + Ansi.RESET + Ansi.GREEN + " to return to the menu.\n" + Ansi.RESET);

  }

  public void chooseMode() {
    boolean isWrong = false;
    while (true) {
      System.out.println();
      System.out.println(Ansi.PURPLE + "╔════════════════════════╗" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "║ " + Ansi.GREEN + "Choose game difficulty" + Ansi.RESET + Ansi.PURPLE + " ║" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "╚════════════════════════╝" + Ansi.RESET);
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "1. " + Ansi.RESET + Ansi.GREEN + Ansi.BOLD + "Easy");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "2. " + Ansi.RESET + Ansi.YELLOW + Ansi.BOLD+ "Normal");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "3. " + Ansi.RESET + Ansi.RED + Ansi.BOLD + "Hard" + Ansi.RESET);
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "4. " + Ansi.RESET + "Back");
      if (isWrong) System.out.println(Ansi.ERROR + "Wrong choice." + Ansi.RESET);
      System.out.print(Ansi.BOLD_PURPLE + "Your choice -> " + Ansi.RESET);

      String line = scanner.nextLine().trim();

      if (line.isEmpty()) {
        isWrong = true;
        continue;
      }

      char input = line.charAt(0);
      isWrong = false;

      boolean isReturn = false;

      Puzzle puzzle = null;

      switch (input) {
        case '1':
          puzzle = puzzleRepository.getRandomPuzzle(7, Difficulty.EASY);
          difficulty = Difficulty.EASY;
          break;
        case '2':
          puzzle = puzzleRepository.getRandomPuzzle(9, Difficulty.NORMAL);
          difficulty = Difficulty.NORMAL;
          break;
        case '3':
          puzzle = puzzleRepository.getRandomPuzzle(5, Difficulty.HARD);
          difficulty = Difficulty.HARD;
          break;
        case '4':
          isReturn = true;
          return;
        default:
          isWrong = true;
          break;
      }
      if (puzzle != null && !isReturn) {
        board = new Board(puzzle);
        break;
      }
    }
  }

  public void topScores() {
    while(true) {
      List<Score> scores = scoreService.getTopScores("jigsaw sudoku");

      System.out.println();
      System.out.println(Ansi.PURPLE + "╔═══════════════╗");
      System.out.println(Ansi.PURPLE + "║ " + Ansi.GREEN + "Top" + Ansi.RESET + " 10 " + Ansi.GREEN + "scores " + Ansi.RESET + Ansi.PURPLE + "║" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "╚═══════════════╝" + Ansi.RESET);

      int i = 0;
      for (Score score : scores) {
        System.out.println(Ansi.GRAY + ++i + ". " +
                Ansi.ITALIC_GREEN + score.getPlayer() + Ansi.RESET + " | " +
                Ansi.PURPLE + score.getFormattedDate() + Ansi.RESET + " | " +
                score.getPoints() + Ansi.PURPLE + " points" + Ansi.RESET
        );
      }

      if (scores.isEmpty()) {
        System.out.println("Leaderboard is empty");
      }

      System.out.print("\nPress Enter to continue...");
      scanner.nextLine();
      break;
    }
  }

  public void comments() {
    boolean isWrong = false;
    while(true) {
      System.out.println();
      System.out.println(Ansi.PURPLE + "╔══════════╗" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "║ " + Ansi.GREEN + "Comments" + Ansi.RESET + Ansi.PURPLE + " ║" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "╚══════════╝" + Ansi.RESET);
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "1. " + Ansi.RESET + "View comments");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "2. " + Ansi.RESET + "Write a comment");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "3. " + Ansi.RESET + "Back");
      if (isWrong) System.out.println(Ansi.ERROR + "Wrong choice." + Ansi.RESET);
      System.out.print(Ansi.BOLD_PURPLE + "Your choice -> " + Ansi.RESET);

      String line = scanner.nextLine().trim();

      if (line.isEmpty()) {
        isWrong = true;
        continue;
      }

      char input = line.charAt(0);
      isWrong = false;

      switch (input) {
        case '1':
          showComments();
          break;
        case '2':
          writeComment();
          break;
        case '3':
          return;
        default:
          isWrong = true;
          break;
      }
    }
  }

  private void showComments() {
    System.out.println();
    System.out.println(Ansi.PURPLE + "╔══════════════╗" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "║" + Ansi.ITALIC_GREEN + " All comments " + Ansi.RESET + Ansi.PURPLE + "║" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "╚══════════════╝" + Ansi.RESET);

    List<Comment> comments = commentService.getComments("jigsaw sudoku");
    for (Comment comment : comments) {
      System.out.println(Ansi.GRAY + "─".repeat(comment.getText().length() + comment.getFormattedCreatedAt().length() + comment.getUserName().length() + 4) + "┐" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + comment.getFormattedCreatedAt() + " " + Ansi.RESET + Ansi.GREEN + comment.getUserName() + ": " + Ansi.RESET + comment.getText() + Ansi.GRAY + " │" + Ansi.RESET);
      System.out.println(Ansi.GRAY + "─".repeat(comment.getText().length() + comment.getFormattedCreatedAt().length() + comment.getUserName().length() + 4) + "┘" + Ansi.RESET);
    }

    System.out.print(Ansi.RESET + "\nPress Enter to continue...");
    scanner.nextLine();
  }

  private void writeComment() {
    System.out.println();
    System.out.println(Ansi.PURPLE + "╔═══════════════╗" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "║" + Ansi.ITALIC_GREEN + " Write comment " + Ansi.RESET + Ansi.PURPLE + "║" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "╚═══════════════╝\n" + Ansi.RESET);

    System.out.print(Ansi.BOLD_PURPLE + "Enter your name (leave empty to back) -> ");
    String name = scanner.nextLine().trim();

    if (name.isEmpty()) {
      return;
    }

    System.out.print("Enter comment text (leave empty to back) -> ");
    String content = scanner.nextLine().trim();

    if (content.isEmpty()) {
      return;
    }

    Comment comment = new Comment("jigsaw sudoku", name, content, new Date());
    commentService.addComment(comment);

    System.out.println(Ansi.GREEN + "Your comment has been saved.");

    System.out.print(Ansi.RESET + "\nPress Enter to continue...");
    scanner.nextLine();
  }

  public void ratings() {
    boolean isWrong = false;
    while(true) {
      System.out.println();
      System.out.println(Ansi.PURPLE + "╔═════════╗" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "║ " + Ansi.GREEN + "Ratings" + Ansi.RESET + Ansi.PURPLE + " ║" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "╚═════════╝" + Ansi.RESET);
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "1. " + Ansi.RESET + "View ratings");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "2. " + Ansi.RESET + "Leave rating");
      System.out.println(Ansi.ITALIC + Ansi.GRAY + "3. " + Ansi.RESET + "Back");
      if (isWrong) System.out.println(Ansi.ERROR + "Wrong choice." + Ansi.RESET);
      System.out.print(Ansi.BOLD_PURPLE + "Your choice -> " + Ansi.RESET);

      String line = scanner.nextLine().trim();

      if (line.isEmpty()) {
        isWrong = true;
        continue;
      }

      char input = line.charAt(0);
      isWrong = false;

      switch (input) {
        case '1':
          showRatings();
          break;
        case '2':
          leaveRating();
          break;
        case '3':
          return;
        default:
          isWrong = true;
          break;
      }
    }
  }

  private void showRatings() {
    System.out.println();
    System.out.println(Ansi.PURPLE + "╔═════════════╗" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "║" + Ansi.ITALIC_GREEN + " All ratings " + Ansi.RESET + Ansi.PURPLE + "║" + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "╚═════════════╝" + Ansi.RESET);

    double averageRating = ratingService.getAverageRating("jigsaw sudoku");
    System.out.println(Ansi.GRAY   + "─────────────────────────" + (averageRating == 10 ? "─┐":"┐") + Ansi.RESET);
    System.out.println(Ansi.PURPLE + "Average game rating: " + averageRating + "/5" + Ansi.GRAY + " │" + Ansi.RESET);
    System.out.println(Ansi.GRAY   + "─────────────────────────" + (averageRating == 10 ? "─┘":"┘") + Ansi.RESET);

    System.out.print(Ansi.RESET + "\nPress Enter to continue...");
    scanner.nextLine();
  }

  private void leaveRating() {
    while (true) {
      System.out.println();
      System.out.println(Ansi.PURPLE + "╔══════════════╗" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "║" + Ansi.ITALIC_GREEN + " Leave rating " + Ansi.RESET + Ansi.PURPLE + "║" + Ansi.RESET);
      System.out.println(Ansi.PURPLE + "╚══════════════╝\n" + Ansi.RESET);

      System.out.print(Ansi.BOLD_PURPLE + "Enter your name (leave empty to return) -> ");
      String name = scanner.nextLine().trim();

      if (name.isEmpty()) {
        break;
      }

      int userRating = ratingService.getRating("jigsaw sudoku", name);

      if (userRating != 0) {
        System.out.println(Ansi.GRAY + "Your current rating is -> " + userRating + "/5" + Ansi.PURPLE);
      }

      System.out.print("Enter rating value (1-5 or leave empty to return) -> ");
      String value = scanner.nextLine().trim();

      if (value.isEmpty()) {
        break;
      }

      int number = 0;
      try {
        number = Integer.parseInt(value);
      } catch (Exception ex) {
        System.out.println(Ansi.ERROR + "Input accepts numbers only." + Ansi.RESET);
        continue;
      }

      if (number > 5 || number < 1) {
        System.out.println(Ansi.ERROR + "Wrong input. Value has to be in range of 1 to 5" + Ansi.RESET);
        continue;
      }


      Rating rating = new Rating("jigsaw sudoku", name, number, new Date());
      ratingService.setRating(rating);

      System.out.println(Ansi.GREEN + "Your rating has been saved.");

      System.out.print(Ansi.RESET + "\nPress Enter to continue...");
      scanner.nextLine();
      break;
    }
  }

  public boolean handleInput() {
    System.out.print(Ansi.BOLD_PURPLE + "Your turn -> " + Ansi.RESET);
    String input = scanner.nextLine().trim();
    if (input.equalsIgnoreCase("q") ||
            input.equalsIgnoreCase("quit") ||
            input.equalsIgnoreCase("exit")) {
      System.exit(0);
    }


    if (input.equalsIgnoreCase("back") || input.equalsIgnoreCase("b")) {
      return false;
    }

    if (input.equalsIgnoreCase("solve") || input.equalsIgnoreCase("s")) {
      board.solve();
      return true;
    }


    String[] tokens = input.split("\\s+");

    if (tokens[0].equalsIgnoreCase("hint") || tokens[0].equalsIgnoreCase("h")) {
      if (tokens.length != 2) {
        System.out.println(Ansi.ERROR + "Invalid input. Use format: hint row" + Ansi.RESET);
        return true;
      }

      if (Integer.parseInt(tokens[1]) < 1 || Integer.parseInt(tokens[1]) > 5) {
        System.out.println(Ansi.ERROR + "Out of border. Use range of 1 to 5" + Ansi.RESET);
        return true;
      }

      List<String> remaining = board.showRowRemaining(Integer.parseInt(tokens[1]) - 1);
      System.out.print("For row " + Ansi.BOLD_PURPLE + tokens[1] + Ansi.RESET + " you have");
      for (String item : remaining) {
        System.out.print(Ansi.GREEN + " " + item + Ansi.RESET);
      }
      System.out.println(" remaining\n");
      return true;
    }

    if (tokens.length != 3) {
      System.out.println(Ansi.ERROR + "Invalid input. Use format: row col value" + Ansi.RESET);
      return true;
    }

    try {
      int row = Integer.parseInt(tokens[0]) - 1;
      int col = Integer.parseInt(tokens[1]) - 1;
      int value = Integer.parseInt(tokens[2]);

      boolean success = board.setValue(row, col, value);

      if (!success) {
        System.out.println(Ansi.ERROR + "Invalid move." + Ansi.RESET);
      }

    } catch (NumberFormatException e) {
      System.out.println(Ansi.ERROR + "Game accepts numbers only." + Ansi.RESET);
    }

    return true;
  }

  public String formatDuration(long minutes, long seconds) {
    if (minutes == 0) {
      return Ansi.RESET + seconds + Ansi.GREEN + " " + (seconds == 1 ? "second" : "seconds");
    }

    if (seconds == 0) {
      return Ansi.RESET + minutes + Ansi.GREEN + " " + (minutes == 1 ? "minute" : "minutes");
    }

    return Ansi.RESET + minutes + Ansi.GREEN + "m " + Ansi.RESET + seconds + Ansi.GREEN + "s" + Ansi.RESET;
  }

  private String formatDifficulty(Difficulty difficulty) {
    return switch (difficulty) {
      case EASY -> "Easy";
      case NORMAL -> "Normal";
      case HARD -> "Hard";
    };
  }

  private void show() {
    System.out.println(board);
  }

  private int calculateScore(long seconds, String difficulty) {
    int difficultyMultiplier = switch (difficulty.trim().toUpperCase()) {
      case "NORMAL" -> 2;
      case "HARD" -> 3;
      default -> 1;
    };

    int baseScore = 10000;
    long timePenalty = seconds * 10;

    long score = (baseScore - timePenalty) * difficultyMultiplier;

    return Math.max((int)score, 0);
  }

  public void saveScore() {
    scoreService.addScore(
            new Score(
                    "jigsaw sudoku",
                    System.getProperty("user.name"),
                    calculateScore(fullSeconds, difficulty.toString()),
                    new Date()
            )
    );
  }
}
