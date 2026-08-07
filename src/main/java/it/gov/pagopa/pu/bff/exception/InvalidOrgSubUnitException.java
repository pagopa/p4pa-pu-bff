package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class InvalidOrgSubUnitException extends BaseBusinessException {
  public InvalidOrgSubUnitException(String code, String message) {
    super(code, message);
  }
}
