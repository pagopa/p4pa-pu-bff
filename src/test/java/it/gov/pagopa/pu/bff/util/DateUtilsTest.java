package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
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

  @Test
  void testToOffsetDateTimeEndOfTheDay() {
    OffsetDateTime expected = LocalDateTime.of(LocalDate.now(),
        LocalTime.MAX.truncatedTo(java.time.temporal.ChronoUnit.MILLIS))
      .atZone(Constants.ZONEID).toOffsetDateTime();

    OffsetDateTime result = DateUtils.toOffsetDateTimeEndOfTheDay(
      LocalDate.now());

    assertTrue(expected.isEqual(result));
  }

  @Test
  void testToOffsetDateTimeStartOfTheDay() {
    OffsetDateTime expected = LocalDate.now().atStartOfDay(Constants.ZONEID).toOffsetDateTime();

    OffsetDateTime result = DateUtils.toOffsetDateTimeStartOfTheDay(
      LocalDate.now());

    assertTrue(expected.isEqual(result));
  }

  @Test
  void givenNullLocalDateWhenToOffsetDateTimeEndOfTheDayThenReturnNull() {
    assertNull(DateUtils.toOffsetDateTimeEndOfTheDay(null));
  }

  @Test
  void givenNullLocalDateWhenToOffsetDateTimeStartOfTheDayThenReturnNull() {
    assertNull(DateUtils.toOffsetDateTimeStartOfTheDay(null));
  }

  @Test
  void testToRangeClosedOffsetDateTimeIntervalFilter() {
    OffsetDateTime from = LocalDate.now().atStartOfDay(Constants.ZONEID).toOffsetDateTime();
    OffsetDateTime to = LocalDateTime.of(LocalDate.now(),
        LocalTime.MAX.truncatedTo(java.time.temporal.ChronoUnit.MILLIS))
      .atZone(Constants.ZONEID).toOffsetDateTime();

    OffsetDateTimeIntervalFilter expected = new OffsetDateTimeIntervalFilter(from, to);

    it.gov.pagopa.pu.processexecutions.dto.generated.OffsetDateTimeIntervalFilter result = DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(
      new LocalDateIntervalFilter(LocalDate.now(), LocalDate.now()));

    assertNotNull(result);
    assertEquals(expected.getFrom(), result.getFrom());
    assertEquals(expected.getTo(), result.getTo());
  }

  @Test
  void givenBothDatesNull_thenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidDateRange(null, null));
  }

  @Test
  void givenOnlyFromDate_thenReturnTrue() {
    OffsetDateTime from = OffsetDateTime.now();
    assertTrue(DateUtils.isNullOrInvalidDateRange(from, null));
  }

  @Test
  void givenOnlyToDate_thenReturnTrue() {
    OffsetDateTime to = OffsetDateTime.now();
    assertTrue(DateUtils.isNullOrInvalidDateRange(null, to));
  }

  @Test
  void givenBothDatesPresent_thenReturnFalse() {
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = OffsetDateTime.now();
    assertFalse(DateUtils.isNullOrInvalidDateRange(from, to));
  }

}
