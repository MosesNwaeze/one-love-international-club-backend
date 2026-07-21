package com.one_love_international_club.exception;

import com.one_love_international_club.dto.Response;
import com.one_love_international_club.dto.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {


  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Response<String>> handleSQLException(DataIntegrityViolationException ex) {

    HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    log.error("Bad Request: ", ex);

    return new ResponseEntity<>(
      Response.<String>builder().status(Status.ERROR)
        .data(ex.getMostSpecificCause().getMessage()).build(),
      statusCode
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Response<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {

    HttpStatus statusCode = HttpStatus.NOT_FOUND;

    return new ResponseEntity<>(
      Response.<String>builder().status(Status.ERROR)
        .data(ex.getMessage().split(":")[0]).build(),
      statusCode
    );

  }

  @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
  public ResponseEntity<String> handleHibernateConstraint(org.hibernate.exception.ConstraintViolationException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body("Constraint violated: " + ex.getConstraintName());
  }

  @ExceptionHandler(ClubException.class)
  public ResponseEntity<Response<String>> handleVDTException(ClubException ex) {

    HttpStatus statusCode = switch (ex.getErrorCode()) {
      case ENTITY_NOT_FOUND -> HttpStatus.NOT_FOUND;
      case DUPLICATE_KEY -> HttpStatus.CONFLICT;
      case NOT_AUTHENTICATED -> HttpStatus.FORBIDDEN;
      case UNAUTHORIZED, INVALID_2FA -> HttpStatus.UNAUTHORIZED;
      case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };

    return new ResponseEntity<>(
      Response.<String>builder().status(Status.ERROR)
        .data(ex.getMessage().split(":")[0]).build(),
      statusCode
    );

  }


  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Response<String>> handleBadCredentialException(BadCredentialsException ex) {

    HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

    return new ResponseEntity<>(
      Response.<String>builder().status(Status.ERROR)
        .data(ex.getMessage().split(":")[0]).build(),
      statusCode
    );

  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Response<String>> handleAccessDeniedException(AccessDeniedException ex) {

    HttpStatus statusCode = HttpStatus.FORBIDDEN;

    return new ResponseEntity<>(
      Response.<String>builder().status(Status.ERROR)
        .data(ex.getMessage().split(":")[0]).build(),
      statusCode
    );

  }


}
