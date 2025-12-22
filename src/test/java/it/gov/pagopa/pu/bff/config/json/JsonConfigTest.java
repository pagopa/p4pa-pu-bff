package it.gov.pagopa.pu.bff.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

class JsonConfigTest {

  private final JsonConfig jsonConfig = new JsonConfig();

  private final ObjectMapper j2ObjectMapper = jsonConfig.objectMapper();
  private final JsonMapper j3JsonMapper = jsonConfig.objectMapperJackson3();

  @Data
  @NoArgsConstructor
  @JsonPropertyOrder({"name", "nullField", "nonNullableNullField", "value", "dateTime", "offsetDateTime"})
  public static class SampleDTO {
    public String name;
    private String nullField;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String nonNullableNullField;
    public Integer value;
    public LocalDateTime dateTime;
    public OffsetDateTime offsetDateTime;
  }

  @Test
  void testJackson2Jackson3ConfigurationAlignment() throws JsonProcessingException {
    // Given
    SampleDTO dto = new SampleDTO();
    dto.setName("NAME");
    dto.setValue(42);
    dto.setDateTime(LocalDateTime.now());
    dto.setOffsetDateTime(OffsetDateTime.now());

    // When
    String j2Serialized = j2ObjectMapper.writeValueAsString(dto);
    String j3Serialized = j3JsonMapper.writeValueAsString(dto);

    // Then
    Assertions.assertEquals(j2Serialized, j3Serialized);
  }
}
