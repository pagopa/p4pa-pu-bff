package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionSearchClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionSearchClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelDebtPosition getDebtPositionByDebtPositionTypeOrgId(Long debtPositionTypeOrgId, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getDebtPositionSearchControllerApi(accessToken)
      .crudDebtPositionsFindByDebtPositionTypeOrgId(
        debtPositionTypeOrgId,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }
}

