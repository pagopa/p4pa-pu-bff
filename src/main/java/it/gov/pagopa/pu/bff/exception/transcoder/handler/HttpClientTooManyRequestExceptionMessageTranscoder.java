package it.gov.pagopa.pu.bff.exception.transcoder.handler;

import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO;
import it.gov.pagopa.pu.bff.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.pu.bff.exception.transcoder.ExceptionMessageTranscoder;
import org.springframework.web.client.HttpClientErrorException;

public class HttpClientTooManyRequestExceptionMessageTranscoder implements ExceptionMessageTranscoder<HttpClientErrorException.TooManyRequests> {
  @Override
  public ExceptionMessageTranscoded transcode(HttpClientErrorException.TooManyRequests tooManyRequestsException) {
    return new ExceptionMessageTranscoded(
      ErrorDTO.CategoryEnum.TOO_MANY_REQUESTS.getValue(),
      tooManyRequestsException.getMessage(),
      null);
  }
}
