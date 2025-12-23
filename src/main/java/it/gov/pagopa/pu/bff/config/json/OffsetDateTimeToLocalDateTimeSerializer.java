package it.gov.pagopa.pu.bff.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.gov.pagopa.pu.bff.util.DateUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class OffsetDateTimeToLocalDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

  @Override
  public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value != null) {
      gen.writeString(formatLocalDateTime(value));
    }
  }

  public static String formatLocalDateTime(OffsetDateTime value) {
    LocalDateTime localDateTime = DateUtils.toLocalDateTime(value);
    return localDateTime.format(DATE_TIME_FORMATTER);
  }
}

