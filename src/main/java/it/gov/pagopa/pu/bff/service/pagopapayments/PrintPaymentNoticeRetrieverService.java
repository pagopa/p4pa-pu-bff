package it.gov.pagopa.pu.bff.service.pagopapayments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;

public interface PrintPaymentNoticeRetrieverService {
  FileResourceDTO generateNotice(Long organizationId, String iuv, DebtPositionDTO debtPositionDTO,
    UserInfo loggedUser, String accessToken);
}
