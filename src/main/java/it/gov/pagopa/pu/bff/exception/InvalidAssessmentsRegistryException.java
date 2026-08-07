package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidAssessmentsRegistryException extends BaseBusinessException {
  public InvalidAssessmentsRegistryException(String code, String message) {
    super(code, message);
  }
}

