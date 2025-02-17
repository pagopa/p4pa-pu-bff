package it.gov.pagopa.pu.bff.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class DateUtils {
  private DateUtils(){}

  public static LocalDateTime toLocalDateTime(OffsetDateTime date){
    return date!=null?date.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime():null;
  }
}
