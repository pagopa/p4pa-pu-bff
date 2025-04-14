package it.gov.pagopa.pu.bff.exception;

public class InvalidDebtPositionException extends RuntimeException {
  public InvalidDebtPositionException(String message) {
    super(message);
  }
}

