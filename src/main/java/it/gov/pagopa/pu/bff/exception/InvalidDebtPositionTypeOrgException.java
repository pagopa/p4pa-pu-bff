package it.gov.pagopa.pu.bff.exception;

public class InvalidDebtPositionTypeOrgException extends RuntimeException implements HasErrorCode {
  private final String code;

  public InvalidDebtPositionTypeOrgException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}

