package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import java.util.List;

public interface DebtPositionTypeOrgRetrieverService {
  List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, UserInfo loggedUser, String accessToken);
}
