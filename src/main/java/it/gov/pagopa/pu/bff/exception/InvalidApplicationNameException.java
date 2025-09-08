package it.gov.pagopa.pu.bff.exception;

public class InvalidApplicationNameException extends RuntimeException {
  public InvalidApplicationNameException(String message) {
    super(message);
  }
}
