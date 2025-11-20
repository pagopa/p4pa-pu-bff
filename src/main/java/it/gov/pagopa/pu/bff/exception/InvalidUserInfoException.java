package it.gov.pagopa.pu.bff.exception;

public class InvalidUserInfoException extends RuntimeException {
  public InvalidUserInfoException(String message) {
    super(message);
  }
}
