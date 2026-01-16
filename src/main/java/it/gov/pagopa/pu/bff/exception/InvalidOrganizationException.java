package it.gov.pagopa.pu.bff.exception;

public class InvalidOrganizationException extends BaseBusinessException {

  public InvalidOrganizationException(String code, String message) {
    super(code, message);
  }
}

