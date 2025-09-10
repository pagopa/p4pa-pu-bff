package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperatorsDptoCountView;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DebtPositionTypeOrgOperatorsDptoCountViewClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgOperatorsDptoCountViewClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public List<DebtPositionTypeOrgOperatorsDptoCountView> findByOrganizationIdAndOperatorExternalUserIds(Long organizationId, Set<String> operatorIds, String accessToken) {
    CollectionModelDebtPositionTypeOrgOperatorsDptoCountView collectionModelDebtPositionTypeOrgOperators = debtPositionApisHolder.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken)
            .crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(organizationId,operatorIds);
    return collectionModelDebtPositionTypeOrgOperators!=null && collectionModelDebtPositionTypeOrgOperators.getEmbedded() !=null?
            collectionModelDebtPositionTypeOrgOperators.getEmbedded().getDebtPositionTypeOrgOperatorsDptoCountViews(): Collections.emptyList();
  }
}

