package it.gov.pagopa.pu.bff.exception;

public class InvalidAssessmentsRegistryException extends BaseBusinessException {
  public InvalidAssessmentsRegistryException(String code, String message) {
    super(code, message);
  }
}

