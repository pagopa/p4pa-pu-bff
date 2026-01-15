package it.gov.pagopa.pu.bff.exception;

import lombok.Getter;

@Getter
public abstract class BaseBusinessException extends RuntimeException implements ErrorCodeProvider {

  protected final String code;

  protected BaseBusinessException(String code, String message) {
    super(message);
    this.code = code;
  }
}
