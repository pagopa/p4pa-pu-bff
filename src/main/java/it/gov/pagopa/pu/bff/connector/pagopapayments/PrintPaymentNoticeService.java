package it.gov.pagopa.pu.bff.connector.pagopapayments;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;

public interface PrintPaymentNoticeService {
  FileResourceDTO generateNotice(String iuv, DebtPositionDTO debtPositionDTO, String accessToken);
}
