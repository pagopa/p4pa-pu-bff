package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidDebtPositionException extends BaseBusinessException {
  public InvalidDebtPositionException(String code, String message) {
    super(code, message);
  }
}

