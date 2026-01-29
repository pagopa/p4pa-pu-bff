package it.gov.pagopa.pu.bff.exception;

public class InvalidAssessmentsDetailException extends BaseBusinessException {
  public InvalidAssessmentsDetailException(String code, String message) {
    super(code, message);
  }
}
