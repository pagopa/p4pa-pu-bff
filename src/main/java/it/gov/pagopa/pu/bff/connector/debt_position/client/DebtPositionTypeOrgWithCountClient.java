package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgWithCountClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgWithCountClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description,  Boolean flagActive, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgWithCountSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsWithCountFindByCodeAndDescription(
        organizationId,
        code,
        description,
        flagActive,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
