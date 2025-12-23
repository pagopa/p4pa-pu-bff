package it.gov.pagopa.pu.bff.config.json.jackson3;

import it.gov.pagopa.pu.bff.config.json.OffsetDateTimeToLocalDateTimeSerializer;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.OffsetDateTime;

@Configuration
public class OffsetDateTimeToLocalDateTimeJackson3Serializer extends ValueSerializer<OffsetDateTime> {

  @Override
  public void serialize(OffsetDateTime value, tools.jackson.core.JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
    if(value!=null) {
      gen.writeString(OffsetDateTimeToLocalDateTimeSerializer.formatLocalDateTime(value));
    }
  }
}

