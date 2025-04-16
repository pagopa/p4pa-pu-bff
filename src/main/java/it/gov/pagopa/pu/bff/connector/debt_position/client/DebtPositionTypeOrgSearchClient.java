package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionTypeOrgSearchClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgSearchClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelDebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeId(Long debtPositionTypeId, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsFindByDebtPositionTypeId(
        debtPositionTypeId,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }
}
