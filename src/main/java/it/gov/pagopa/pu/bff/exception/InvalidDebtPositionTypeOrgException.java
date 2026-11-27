package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidDebtPositionTypeOrgException extends BaseBusinessException {
  public InvalidDebtPositionTypeOrgException(String code, String message) {
    super(code, message);
  }
}

