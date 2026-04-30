package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;

public interface DebtPositionNoticeRetrieverService {
  FileResourceDTO getNotice(Long organizationId, String nav, Long debtPositionId,
    UserInfo loggedUser, String accessToken);
}
