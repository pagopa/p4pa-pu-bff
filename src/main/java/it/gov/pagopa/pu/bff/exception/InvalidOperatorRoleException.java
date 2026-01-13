package it.gov.pagopa.pu.bff.exception;

public class InvalidOperatorRoleException extends RuntimeException implements HasErrorCode {
  private final String code;

  public InvalidOperatorRoleException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}
