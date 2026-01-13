package it.gov.pagopa.pu.bff.exception;

public class ResourceNotFoundException extends RuntimeException implements HasErrorCode {
  private final String code;

  public ResourceNotFoundException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}

