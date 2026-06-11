package it.gov.pagopa.pu.bff.exception;

public class InvalidOrgSubUnitException extends BaseBusinessException {
  public InvalidOrgSubUnitException(String code, String message) {
    super(code, message);
  }
}
