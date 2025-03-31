package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;

public class DateFilterUtils {

  private DateFilterUtils(){}
  public static void validateDateFilters(LocalDateIntervalFilter dateFilter, String filterName) {
    if ((dateFilter.getFrom() != null && dateFilter.getTo() == null) ||
      (dateFilter.getFrom() == null && dateFilter.getTo() != null)) {
      throw new IllegalArgumentException("Both " + filterName + "From and " + filterName + "To must be set or both must be null");
    }
  }
}
