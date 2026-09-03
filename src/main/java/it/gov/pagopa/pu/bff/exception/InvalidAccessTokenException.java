package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidAccessTokenException extends BaseBusinessException {
  public InvalidAccessTokenException(String code, String message) {
    super(code, message);
  }
}
