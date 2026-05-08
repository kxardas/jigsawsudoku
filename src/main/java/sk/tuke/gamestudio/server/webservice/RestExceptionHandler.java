package sk.tuke.gamestudio.server.webservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.tuke.gamestudio.server.service.auth.AuthException;

@RestControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
    return error(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException exception) {
    return error(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ErrorResponse> handleAuth(AuthException exception) {
    return error(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ErrorResponse(message));
  }

  public record ErrorResponse(String message) {
  }
}
