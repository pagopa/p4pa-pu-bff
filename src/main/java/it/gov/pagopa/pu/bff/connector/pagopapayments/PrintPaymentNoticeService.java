package it.gov.pagopa.pu.bff.connector.pagopapayments;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;

public interface PrintPaymentNoticeService {
  FileResourceDTO generateNotice(String iuv, DebtPositionDetailDTO debtPositionDTO, String accessToken);
}
