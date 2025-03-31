package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateFilterUtilsTest {

  @Test
  void whenBothDatesAreSetThenNoException() {
    LocalDate from = LocalDate.now().minusDays(10);
    LocalDate to = LocalDate.now();
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(from, to);

    assertDoesNotThrow(() -> DateFilterUtils.validateDateFilters(dateFilter, "testDate"));
  }

  @Test
  void whenBothDatesAreNullThenNoException() {
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(null, null);

    assertDoesNotThrow(() -> DateFilterUtils.validateDateFilters(dateFilter, "testDate"));
  }

  @Test
  void whenOnlyFromDateIsSetThenThrowException() {
    LocalDate from = LocalDate.now().minusDays(10);
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(from, null);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> DateFilterUtils.validateDateFilters(dateFilter, "testDate"));

    String expectedMessage = "Both testDateFrom and testDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenOnlyToDateIsSetThenThrowException() {
    LocalDate to = LocalDate.now();
    LocalDateIntervalFilter dateFilter = new LocalDateIntervalFilter(null, to);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> DateFilterUtils.validateDateFilters(dateFilter, "testDate"));

    String expectedMessage = "Both testDateFrom and testDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
