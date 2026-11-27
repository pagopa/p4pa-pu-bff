package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidOrgSilServiceException extends BaseBusinessException {
  public InvalidOrgSilServiceException(String code, String message) {
    super(code, message);
  }
}
