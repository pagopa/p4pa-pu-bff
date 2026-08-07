package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class DebtPositionTypeOrgClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, Boolean flagActive, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(organizationId, operatorExternalUserId, flagActive);
  }

  public DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId,
                                                    String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgEntityControllerApi(accessToken)
        .crudGetDebtpositiontypeorg(String.valueOf(debtPositionTypeOrgId));
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPositionTypeOrg with debtPositionTypeOrgId {} not found", debtPositionTypeOrgId);
      return null;
    }
  }

  public CollectionModelDebtPositionTypeOrgCountByOrganizationId getDebtPositionTypeOrgCountByOrganizationId(
    List<Long> organizationIds, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsByOrganizationCountByOrganizationIds(organizationIds);
  }

  public void deleteDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken){
    try{
      debtPositionApisHolder.getDebtPositionTypeOrgApi(accessToken).deleteDebtPositionTypeOrg(debtPositionTypeOrgId);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "DebtPositionTypeOrg with debtPositionTypeOrgId %d not found".formatted(debtPositionTypeOrgId));
    }
  }

  public DebtPositionTypeOrg saveDebtPositionTypeOrg(SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrg, String accessToken){
    return debtPositionApisHolder.getDebtPositionTypeOrgApi(accessToken).saveDebtPositionTypeOrg(saveDebtPositionTypeOrg);
  }

  public void updateFlagActiveDebtPositionTypeOrg(Long debtPositionTypeOrgId, Boolean flagActive,  String accessToken){
    try{
      debtPositionApisHolder.getDebtPositionTypeOrgApi(accessToken).updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgId, flagActive);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "DebtPositionTypeOrg with debtPositionTypeOrgId %d not found".formatted(debtPositionTypeOrgId));
    }
  }

  public CollectionModelDebtPositionTypeOrg getByDebtPositionTypeOrgIdIn(Set<Long> debtPositionTypeOrgIds, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsFindByDebtPositionTypeOrgIdIn(debtPositionTypeOrgIds);
  }
}

