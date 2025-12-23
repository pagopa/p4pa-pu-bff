package it.gov.pagopa.pu.bff.config.json.jackson3;

import it.gov.pagopa.pu.bff.config.json.OffsetDateTimeToLocalDateTimeSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import tools.jackson.core.JsonGenerator;

import java.time.OffsetDateTime;

class OffsetDateTimeToLocalDateTimeJackson3SerializerTest {

  private final OffsetDateTimeToLocalDateTimeJackson3Serializer serializer = new OffsetDateTimeToLocalDateTimeJackson3Serializer();

  @Test
  void whenDeserializeThenCallHandler(){
    try(MockedStatic<OffsetDateTimeToLocalDateTimeSerializer> serializerStatic = Mockito.mockStatic(OffsetDateTimeToLocalDateTimeSerializer.class)){
      OffsetDateTime value = OffsetDateTime.now();
      String expectedResult = "FORMATTEDLOCALDATETIME";
      JsonGenerator gen = Mockito.mock(JsonGenerator.class);

      serializerStatic.when(()-> OffsetDateTimeToLocalDateTimeSerializer.formatLocalDateTime(value))
          .thenReturn(expectedResult);

      serializer.serialize(value, gen, null);

      Mockito.verify(gen)
        .writeString(expectedResult);
    }
  }

  @Test
  void givenNullWhenDeserializeThenDoNothing(){
    JsonGenerator gen = Mockito.mock(JsonGenerator.class);

    serializer.serialize(null, gen, null);

    Mockito.verifyNoInteractions(gen);
  }
}
