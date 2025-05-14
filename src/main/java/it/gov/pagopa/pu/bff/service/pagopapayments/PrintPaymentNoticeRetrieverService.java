package it.gov.pagopa.pu.bff.service.pagopapayments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;

public interface PrintPaymentNoticeRetrieverService {
  FileResourceDTO generateNotice(Long organizationId, String iuv, Long debtPositionId,
    UserInfo loggedUser, String accessToken);
}
