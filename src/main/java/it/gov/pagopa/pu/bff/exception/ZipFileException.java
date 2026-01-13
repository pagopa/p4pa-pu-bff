package it.gov.pagopa.pu.bff.exception;

public class ZipFileException extends RuntimeException implements HasErrorCode {
  private final String code;

  public ZipFileException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }
}
