package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidAssessmentsDetailException extends BaseBusinessException {
  public InvalidAssessmentsDetailException(String code, String message) {
    super(code, message);
  }
}
