package it.gov.pagopa.pu.bff.exception.transcoder.handler;

import it.gov.pagopa.pu.bff.exception.common.BaseBusinessException;
import it.gov.pagopa.pu.bff.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.pu.bff.exception.transcoder.ExceptionMessageTranscoder;

public class BaseBusinessExceptionMessageTranscoder implements ExceptionMessageTranscoder<BaseBusinessException> {
  @Override
  public ExceptionMessageTranscoded transcode(BaseBusinessException businessException) {
    return new ExceptionMessageTranscoded(businessException.getCode(), businessException.getMessage(), businessException.getFields());
  }
}
