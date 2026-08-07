package it.gov.pagopa.pu.bff.exception;

public class InvalidPdndClientException extends BaseBusinessException {
  public InvalidPdndClientException(String code, String message) {
    super(code, message);
  }
}
