package it.gov.pagopa.pu.bff.util;

import static it.gov.pagopa.pu.bff.util.Constants.ZONEID;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;

import it.gov.pagopa.pu.processexecutions.dto.generated.OffsetDateTimeIntervalFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class DateUtils {
  private DateUtils(){}

  public static LocalDateTime toLocalDateTime(OffsetDateTime date){
    return date!=null?date.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime():null;
  }

  public static void validateDateFilters(LocalDateIntervalFilter dateFilter, String filterName) {
    if ((dateFilter.getFrom() != null && dateFilter.getTo() == null) ||
      (dateFilter.getFrom() == null && dateFilter.getTo() != null)) {
      throw new IllegalArgumentException("Both " + filterName + "From and " + filterName + "To must be set or both must be null");
    }
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
}
