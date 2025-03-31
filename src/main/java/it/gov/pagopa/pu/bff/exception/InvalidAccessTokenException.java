package it.gov.pagopa.pu.bff.exception;

public class InvalidAccessTokenException extends RuntimeException {
  public InvalidAccessTokenException(String message) {
    super(message);
  }
}
