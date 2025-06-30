package it.gov.pagopa.pu.bff.exception;

public class InvalidParameterException extends RuntimeException {
  public InvalidParameterException(String message) {
    super(message);
  }
}
