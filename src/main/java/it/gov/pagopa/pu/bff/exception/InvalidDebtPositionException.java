package it.gov.pagopa.pu.bff.exception;

public class InvalidDebtPositionException extends BaseBusinessException {
  public InvalidDebtPositionException(String code, String message) {
    super(code, message);
  }
}

