package it.gov.pagopa.pu.bff.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.gov.pagopa.pu.bff.util.DateUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Configuration
public class OffsetDateTimeToLocalDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

  @Override
  public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value != null) {
      LocalDateTime localDateTime = value.atZoneSameInstant(DateUtils.europeRomeZoneId).toLocalDateTime();
      gen.writeString(localDateTime.toString());
    }
  }
}

