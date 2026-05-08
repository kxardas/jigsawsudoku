package sk.tuke.gamestudio.server.dto.auth;

public class AuthResponseDto {
  private UserDto user;

  public AuthResponseDto() { }
  public AuthResponseDto(UserDto user) { this.user = user; }
  public UserDto getUser() { return user; }
  public void setUser(UserDto user) { this.user = user; }
}
