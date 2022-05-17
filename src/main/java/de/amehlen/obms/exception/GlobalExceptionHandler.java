package de.amehlen.obms.exception;

import de.amehlen.obms.dto.ErrorMessageDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessageDTO> handleUserNotFoundException(
      UserNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorMessageDTO.builder()
            .withTitle("User Not Found Exception")
            .withMessage(exception.getMessage())
            .withStatus(HttpStatus.NOT_FOUND.value())
            .withErrorType(UserNotFoundException.class.getSimpleName())
            .build(),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorMessageDTO> handleUserAlreadyExistException(
      UserAlreadyExistException exception) {
    return new ResponseEntity<>(
        ErrorMessageDTO.builder()
            .withTitle("User Already Exist")
            .withMessage(exception.getMessage())
            .withStatus(HttpStatus.CONFLICT.value())
            .withErrorType(UserAlreadyExistException.class.getSimpleName())
            .build(),
        HttpStatus.CONFLICT
    );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}
