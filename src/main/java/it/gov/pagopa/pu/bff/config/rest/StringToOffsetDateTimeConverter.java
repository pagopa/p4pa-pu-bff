package it.gov.pagopa.pu.bff.config.rest;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

  public OffsetDateTime convert(String source) {
    if (!source.endsWith("Z") && !source.contains("+")) {
      LocalDateTime ldt = LocalDateTime.parse(source);
      return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else {
      return OffsetDateTime.parse(source);
    }
  }
}
