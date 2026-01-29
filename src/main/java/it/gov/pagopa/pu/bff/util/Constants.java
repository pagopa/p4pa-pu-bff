package it.gov.pagopa.pu.bff.util;

import java.time.ZoneId;
import java.util.TimeZone;

public class Constants {

  private Constants(){}

  public static final ZoneId ZONEID = ZoneId.of("Europe/Rome");
  public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(ZONEID);

  public static final String INSTALLMENT_REMITTANCE_INFORMATION_PLACEHOLDER = "Pagamento on-the-fly";
}

