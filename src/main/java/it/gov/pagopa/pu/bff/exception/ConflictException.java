package it.gov.pagopa.pu.bff.exception;

public class ConflictException extends RuntimeException implements HasErrorCode {
  private final String code;

  public ConflictException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}

