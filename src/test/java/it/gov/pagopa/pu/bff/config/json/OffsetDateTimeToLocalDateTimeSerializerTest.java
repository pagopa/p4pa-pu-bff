package it.gov.pagopa.pu.bff.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OffsetDateTimeToLocalDateTimeSerializerTest {

  @Mock
  private JsonGenerator jsonGenerator;

  @Mock
  private SerializerProvider serializerProvider;

  private OffsetDateTimeToLocalDateTimeSerializer dateTimeSerializer;

  @BeforeEach
  public void setUp() {
    dateTimeSerializer = new OffsetDateTimeToLocalDateTimeSerializer();

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  void testDateSerializer() throws IOException {
    OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.of(2025, 1, 16, 9, 15, 20), ZoneOffset.ofHours(1));

    dateTimeSerializer.serialize(offsetDateTime, jsonGenerator, serializerProvider);

    verify(jsonGenerator).writeString("2025-01-16T08:15:20.000000");
  }

  @Test
  void testNullDateSerializer() throws IOException {
    dateTimeSerializer.serialize(null, jsonGenerator, serializerProvider);

    verifyNoInteractions(jsonGenerator);
  }
}
