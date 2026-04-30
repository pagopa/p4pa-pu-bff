package it.gov.pagopa.pu.bff.util;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

public class Constants {

  private Constants(){}

  public static final ZoneId ZONEID = ZoneId.of("Europe/Rome");
  public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(ZONEID);

  public static final String INSTALLMENT_REMITTANCE_INFORMATION_PLACEHOLDER = "Pagamento on-the-fly";

  public static final List<DebtPositionOrigin> ORDINARY_DEBT_POSITION_ORIGINS = List.of(
    DebtPositionOrigin.ORDINARY,
    DebtPositionOrigin.ORDINARY_SIL,
    DebtPositionOrigin.SPONTANEOUS,
    DebtPositionOrigin.SPONTANEOUS_SIL,
    DebtPositionOrigin.SPONTANEOUS_PSP);
}

