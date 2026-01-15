package it.gov.pagopa.pu.bff.exception;

public class InvalidOrgSilServiceException extends BaseBusinessException {
  public InvalidOrgSilServiceException(String code, String message) {
    super(code, message);
  }
}
