package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.server.dto.ErrorResponseDto;
import sk.tuke.gamestudio.server.service.auth.AuthException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
          ResponseStatusException exception,
          HttpServletRequest request
  ) {
    HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());

    ErrorResponseDto body = new ErrorResponseDto(
            status.value(),
            status.getReasonPhrase(),
            exception.getReason(),
            request.getRequestURI()
    );

    return ResponseEntity.status(status).body(body);
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ErrorResponseDto> handleAuthException(
          AuthException exception,
          HttpServletRequest request
  ) {
    ErrorResponseDto body = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
          IllegalArgumentException exception,
          HttpServletRequest request
  ) {
    ErrorResponseDto body = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalStateException(
          IllegalStateException exception,
          HttpServletRequest request
  ) {
    ErrorResponseDto body = new ErrorResponseDto(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleException(
          Exception exception,
          HttpServletRequest request
  ) {
    ErrorResponseDto body = new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Something went wrong",
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}