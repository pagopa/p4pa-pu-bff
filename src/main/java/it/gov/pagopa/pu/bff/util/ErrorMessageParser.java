package it.gov.pagopa.pu.bff.util;

public final class ErrorMessageParser {
  private ErrorMessageParser() {
  }

  public static ParsedError parse(String message) {
    if (message == null || message.isBlank()) {
      return new ParsedError("GENERIC_ERROR", null);
    }

    String s = message.stripLeading();
    if (!s.startsWith("[")) {
      return new ParsedError("GENERIC_ERROR", message.trim());
    }

    int end = s.indexOf(']');
    if (end <= 1) { // ']' not found or code empty "[]"
      return new ParsedError("GENERIC_ERROR", message.trim());
    }

    String code = s.substring(1, end).trim();
    if (code.isBlank()) code = "GENERIC_ERROR";

    String description = s.substring(end + 1).trim();
    if (description.isBlank()) description = null;

    return new ParsedError(code, description);
  }

  public record ParsedError(String code, String description) {
  }
}

