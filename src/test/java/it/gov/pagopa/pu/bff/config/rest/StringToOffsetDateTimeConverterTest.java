package it.gov.pagopa.pu.bff.config.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.OffsetDateTime;

class StringToOffsetDateTimeConverterTest {

  StringToOffsetDateTimeConverter stringToOffsetDateTimeConverter;

  @BeforeEach
  void setUp() {
    stringToOffsetDateTimeConverter = new StringToOffsetDateTimeConverter();
  }

  @Test
  void givenStringLocalDateTimeWhenConvertThenReturnOffsetDateTime() {
    //given
    String dateTimeToConvert = "2025-05-22T12:56:59";
    //when
    OffsetDateTime result = stringToOffsetDateTimeConverter.convert(dateTimeToConvert);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(OffsetDateTime.parse("2025-05-22T12:56:59+02:00"), result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"2025-05-22T12:56:59+02:00",
                          "2025-05-22T12:56:59Z"})
  void givenUTCAndOffsetDateTimeWhenConvertThenReturnOffsetDateTime(String dateTimeToConvert){
    //when
    OffsetDateTime result = stringToOffsetDateTimeConverter.convert(dateTimeToConvert);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(OffsetDateTime.parse(dateTimeToConvert), result);
  }

}
