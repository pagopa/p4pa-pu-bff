package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidOrganizationException extends BaseBusinessException {

  public InvalidOrganizationException(String code, String message) {
    super(code, message);
  }
}

