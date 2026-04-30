package it.gov.pagopa.pu.bff.exception;

public class InvalidOperatorRoleException extends BaseBusinessException {
  public InvalidOperatorRoleException(String code, String message) {
    super(code, message);
  }
}
