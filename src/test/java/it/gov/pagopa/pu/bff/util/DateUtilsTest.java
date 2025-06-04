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
  void givenBothLocalDatesNullWhenIsNullOrInvalidLocalDateRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateRange(null, null));
  }

  @Test
  void givenOnlyLocalFromDateWhenIsNullOrInvalidLocalDateRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateRange(LocalDate.now(), null));
  }

  @Test
  void givenOnlyLocalToDateWhenIsNullOrInvalidLocalDateRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateRange(null, LocalDate.now()));
  }

  @Test
  void givenBothLocalDatesPresentWhenIsNullOrInvalidLocalDateRangeThenReturnFalse() {
    assertFalse(DateUtils.isNullOrInvalidLocalDateRange(LocalDate.now().minusDays(1), LocalDate.now()));
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
  void givenBothDatesNullWhenIsNullOrInvalidOffsetDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidOffsetDateTimeRange(null, null));
  }

  @Test
  void givenOnlyFromDateWhenIsNullOrInvalidOffsetDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidOffsetDateTimeRange(OffsetDateTime.now(), null));
  }

  @Test
  void givenOnlyToDateWhenIsNullOrInvalidOffsetDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidOffsetDateTimeRange(null, OffsetDateTime.now()));
  }

  @Test
  void givenBothDatesPresentWhenIsNullOrInvalidOffsetDateTimeRangeThenReturnFalse() {
    assertFalse(DateUtils.isNullOrInvalidOffsetDateTimeRange(OffsetDateTime.now().minusDays(1), OffsetDateTime.now()));
  }

  @Test
  void givenBothLocalDateTimesNullWhenIsNullOrInvalidLocalDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateTimeRange(null, null));
  }

  @Test
  void givenOnlyLocalDateTimeFromWhenIsNullOrInvalidLocalDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateTimeRange(LocalDateTime.now(), null));
  }

  @Test
  void givenOnlyLocalDateTimeToWhenIsNullOrInvalidLocalDateTimeRangeThenReturnTrue() {
    assertTrue(DateUtils.isNullOrInvalidLocalDateTimeRange(null, LocalDateTime.now()));
  }

  @Test
  void givenBothLocalDateTimesPresentWhenIsNullOrInvalidLocalDateTimeRangeThenReturnFalse() {
    assertFalse(DateUtils.isNullOrInvalidLocalDateTimeRange(LocalDateTime.now().minusDays(1), LocalDateTime.now()));
  }

}
