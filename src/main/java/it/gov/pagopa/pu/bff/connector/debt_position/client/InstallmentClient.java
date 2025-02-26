package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstallmentClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public InstallmentClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getInstallmentViewSearchControllerApi(accessToken)
      .crudInstallmentViewsFindInstallmentsByFilters(
        installmentViewFiltersDTO.getOrganizationId(),
        installmentViewFiltersDTO.getOperatorExternalUserId(),
        installmentViewFiltersDTO.getDueDate().getFrom(),
        installmentViewFiltersDTO.getDueDate().getTo(),
        installmentViewFiltersDTO.getIuv(),
        installmentViewFiltersDTO.getFiscalCode(),
        installmentViewFiltersDTO.getDebtPositionTypeOrgId(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
