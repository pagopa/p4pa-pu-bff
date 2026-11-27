package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidPdndClientException extends BaseBusinessException {
  public InvalidPdndClientException(String code, String message) {
    super(code, message);
  }
}
