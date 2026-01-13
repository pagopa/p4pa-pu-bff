package it.gov.pagopa.pu.bff.exception;

public class InvalidAssessmentsRegistryException extends RuntimeException implements HasErrorCode {
  private final String code;

  public InvalidAssessmentsRegistryException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}

