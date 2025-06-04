package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.OffsetDateTimeIntervalFilter;

import java.time.*;

import static it.gov.pagopa.pu.bff.util.Constants.ZONEID;

public class DateUtils {
  private DateUtils() {
  }

  public static LocalDateTime toLocalDateTime(OffsetDateTime date) {
    return date != null ? date.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime() : null;
  }

  public static OffsetDateTime toOffsetDateTimeStartOfTheDay(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return localDate.atStartOfDay(ZONEID).toOffsetDateTime();
  }

  public static OffsetDateTime toOffsetDateTimeEndOfTheDay(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX.truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
    return endOfDay.atZone(ZONEID).toOffsetDateTime();
  }

  public static OffsetDateTimeIntervalFilter toRangeClosedOffsetDateTimeIntervalFilter(
    LocalDateIntervalFilter localDateIntervalFilter) {
    return OffsetDateTimeIntervalFilter.builder()
      .from(toOffsetDateTimeStartOfTheDay(localDateIntervalFilter.getFrom()))
      .to(toOffsetDateTimeEndOfTheDay(localDateIntervalFilter.getTo()))
      .build();
  }

  /**
   * Returns true if both dates are null or only one is null
   */
  public static boolean isNullOrInvalidOffsetDateTimeRange(OffsetDateTime from, OffsetDateTime to) {
    return (from == null && to == null) || (from == null ^ to == null);
  }

  public static boolean isNullOrInvalidLocalDateRange(LocalDate from, LocalDate to) {
    return (from == null && to == null) || (from == null ^ to == null);
  }

  public static boolean isNullOrInvalidLocalDateTimeRange(LocalDateTime from, LocalDateTime to) {
    return (from == null && to == null) || (from == null ^ to == null);
  }

}
