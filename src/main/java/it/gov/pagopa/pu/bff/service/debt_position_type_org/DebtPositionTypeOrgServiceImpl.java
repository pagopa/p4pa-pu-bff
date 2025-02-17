package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService {

  private final DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient debtPositionTypeOrgClient) {
    this.debtPositionTypeOrgClient = debtPositionTypeOrgClient;
  }

  public List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);
    CollectionModelDebtPositionTypeOrg collection = debtPositionTypeOrgClient.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getDebtPositionTypeOrgs();
  }

}
