package it.gov.pagopa.pu.bff.exception;

public class InvalidOrganizationException extends RuntimeException {
  public InvalidOrganizationException(String message) {
    super(message);
  }
}

