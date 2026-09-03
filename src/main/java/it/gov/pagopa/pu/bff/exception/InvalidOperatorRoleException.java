package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidOperatorRoleException extends BaseBusinessException {
  public InvalidOperatorRoleException(String code, String message) {
    super(code, message);
  }
}
