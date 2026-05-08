package sk.tuke.gamestudio.server.dto;

public class NewGameRequestDto {
  private int size = 5;
  private String difficulty = "EASY";

  public NewGameRequestDto() { }

  public NewGameRequestDto(String difficulty, int size) {
    this.size = size;
    this.difficulty = difficulty;
  }

  public int getSize() { return size; }
  public void setSize(int size) { this.size = size; }

  public String getDifficulty() { return difficulty; }
  public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
