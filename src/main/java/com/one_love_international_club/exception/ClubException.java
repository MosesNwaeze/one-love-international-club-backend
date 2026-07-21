package com.one_love_international_club.exception;

import lombok.Getter;

@Getter
public class ClubException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String message;

  public ClubException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.message = message;
  }

  public ClubException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
    this.message = message;
  }

  public ClubException(ErrorCode errorCode, Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
    this.message = cause.getMessage();
  }
}
