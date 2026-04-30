package it.gov.pagopa.pu.bff.exception;

public class InvalidUserInfoException extends BaseBusinessException {
  public InvalidUserInfoException(String code, String message) {
    super(code, message);
  }
}
