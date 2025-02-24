package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, List<String> debtPositionOrigins, String operatorExternalId, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getDebtPositionViewSearchControllerApi(accessToken)
      .crudDebtPositionsViewFindDebtPositionViews(
        filtersDTO.getOrganizationId(),
        debtPositionOrigins,
        operatorExternalId,
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateFrom()),
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateTo()),
        filtersDTO.getFiscalCode(),
        filtersDTO.getDebtPositionTypeOrgId(),
        filtersDTO.getStatus()!=null?filtersDTO.getStatus().toString():null,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }
}

