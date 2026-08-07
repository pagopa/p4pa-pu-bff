package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;

public class ZipFileException extends BaseBusinessException {
  public ZipFileException(String code, String message) {
    super(code, message);
  }
}
