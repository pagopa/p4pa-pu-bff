package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgRetrieverServiceImpl implements
  DebtPositionTypeOrgRetrieverService {

  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public DebtPositionTypeOrgRetrieverServiceImpl(DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
  }

  public List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);
    CollectionModelDebtPositionTypeOrg collection = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getDebtPositionTypeOrgs();
  }

}
