package it.gov.pagopa.pu.bff.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateUtilsTest {
  @BeforeEach
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  void givenValidDateWhenToLocalDateTimeThenOk(){
    LocalDateTime date = LocalDateTime.now();
    LocalDateTime expectedDate = date.minusHours(5);

    LocalDateTime result = DateUtils.toLocalDateTime(OffsetDateTime.of(date, ZoneOffset.ofHours(5)));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDate.getYear(),result.getYear());
    Assertions.assertEquals(expectedDate.getMonth(),result.getMonth());
    Assertions.assertEquals(expectedDate.getDayOfMonth(),result.getDayOfMonth());
    Assertions.assertEquals(expectedDate.getHour(),result.getHour());
    Assertions.assertEquals(expectedDate.getMinute(),result.getMinute());
    Assertions.assertEquals(expectedDate.getSecond(),result.getSecond());
  }

  @Test
  void givenNullDateWhenToLocalDateTimeThenNullResult(){
    LocalDateTime result = DateUtils.toLocalDateTime(null);

    Assertions.assertNull(result);
  }
}
