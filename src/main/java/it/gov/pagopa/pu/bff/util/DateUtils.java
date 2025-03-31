package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;

import java.time.LocalDateTime;
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
}
