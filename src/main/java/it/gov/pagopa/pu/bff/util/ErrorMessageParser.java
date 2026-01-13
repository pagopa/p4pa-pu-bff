package it.gov.pagopa.pu.bff.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ErrorMessageParser {
  private ErrorMessageParser() {
  }

  // ONLY if [CODE] is in the beginning (ignoring spaces)
  private static final Pattern LEADING_BRACKET_CODE = Pattern.compile("^\\s*\\[([^\\]]+)]\\s*(.*)$");

  public static ParsedError parse(String message) {
    if (message == null || message.isBlank()) {
      return new ParsedError("GENERIC_ERROR", null);
    }

    Matcher m = LEADING_BRACKET_CODE.matcher(message);
    if (m.matches()) {
      String code = m.group(1) != null ? m.group(1).trim() : "GENERIC_ERROR";
      String description = m.group(2) != null ? m.group(2).trim() : null;

      if (code.isBlank()) code = "GENERIC_ERROR";
      if (description != null && description.isBlank()) description = null;

      return new ParsedError(code, description);
    }

    // If it doesn't start with [CODE], dont't extract anything
    return new ParsedError("GENERIC_ERROR", message.trim());
  }

  public record ParsedError(String code, String description) {
  }
}

