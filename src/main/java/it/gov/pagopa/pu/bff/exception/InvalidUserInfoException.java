package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidUserInfoException extends BaseBusinessException {
  public InvalidUserInfoException(String code, String message) {
    super(code, message);
  }
}
