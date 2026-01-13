package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.util.ErrorMessageParser;

public final class UpstreamErrorMapper {
  private UpstreamErrorMapper() {}

  public record MappedUpstreamError(String code, String description) {}

  public static MappedUpstreamError map(String maybeMessage, String fallbackMessage) {
    String upstreamMessage = chooseMessage(maybeMessage, fallbackMessage);

    ErrorMessageParser.ParsedError parsed =
      ErrorMessageParser.parse(upstreamMessage);

    String description =
      parsed.description() != null ? parsed.description() : upstreamMessage;

    return new MappedUpstreamError(parsed.code(), description);
  }

  private static String chooseMessage(String maybeMessage, String fallback) {
    return (maybeMessage != null && !maybeMessage.isBlank())
      ? maybeMessage
      : fallback;
  }
}

