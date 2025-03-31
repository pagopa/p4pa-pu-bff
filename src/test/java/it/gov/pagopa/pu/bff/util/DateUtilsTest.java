package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {
  @BeforeEach
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  void givenValidDateWhenToLocalDateTimeThenOk() {
    LocalDateTime date = LocalDateTime.now();
    LocalDateTime expectedDate = date.minusHours(5);

    LocalDateTime result = DateUtils.toLocalDateTime(OffsetDateTime.of(date, ZoneOffset.ofHours(5)));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDate.getYear(), result.getYear());
    Assertions.assertEquals(expectedDate.getMonth(), result.getMonth());
    Assertions.assertEquals(expectedDate.getDayOfMonth(), result.getDayOfMonth());
    Assertions.assertEquals(expectedDate.getHour(), result.getHour());
    Assertions.assertEquals(expectedDate.getMinute(), result.getMinute());
    Assertions.assertEquals(expectedDate.getSecond(), result.getSecond());
  }

  @Test
  void givenNullDateWhenToLocalDateTimeThenNullResult() {
    LocalDateTime result = DateUtils.toLocalDateTime(null);

    Assertions.assertNull(result);
  }

  @Test
  void whenBothDatesAreSetThenNoException() {
    LocalDate from = LocalDate.now().minusDays(10);
    LocalDate to = LocalDate.now();
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(from, to);

    assertDoesNotThrow(() -> DateUtils.validateDateFilters(dateFilter, "testDate"));
  }

  @Test
  void whenBothDatesAreNullThenNoException() {
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(null, null);

    assertDoesNotThrow(() -> DateUtils.validateDateFilters(dateFilter, "testDate"));
  }

  @Test
  void whenOnlyFromDateIsSetThenThrowException() {
    LocalDate from = LocalDate.now().minusDays(10);
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(from, null);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> DateUtils.validateDateFilters(dateFilter, "testDate"));

    String expectedMessage = "Both testDateFrom and testDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenOnlyToDateIsSetThenThrowException() {
    LocalDate to = LocalDate.now();
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(null, to);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> DateUtils.validateDateFilters(dateFilter, "testDate"));

    String expectedMessage = "Both testDateFrom and testDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
