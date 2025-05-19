package it.gov.pagopa.pu.bff.exception;

public class PdfProcessingException extends RuntimeException {
  public PdfProcessingException(String message) {
    super(message);
  }
}
