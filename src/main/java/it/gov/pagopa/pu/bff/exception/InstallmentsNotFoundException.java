package it.gov.pagopa.pu.bff.exception;

public class InstallmentsNotFoundException extends RuntimeException {
  public InstallmentsNotFoundException(String message) {
    super(message);
  }
}
