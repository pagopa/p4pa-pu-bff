package it.gov.pagopa.pu.bff.exception.common;

public class NotFoundException extends BaseBusinessException {
  public NotFoundException(String code, String message) {
    super(code, message);
  }
}

