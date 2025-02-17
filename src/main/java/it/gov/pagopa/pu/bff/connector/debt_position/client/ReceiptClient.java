package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReceiptClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public ReceiptClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getReceiptViewSearchControllerApi(accessToken)
      .crudReceiptsViewFindReceiptsByFilters(
        String.valueOf(receiptViewFiltersDTO.getOrganizationId()),
        receiptViewFiltersDTO.getReceiptOrigin().toString(),
        receiptViewFiltersDTO.getOperatorExternalUserId(),
        receiptViewFiltersDTO.getIuv(),
        receiptViewFiltersDTO.getIur(),
        receiptViewFiltersDTO.getIud(),
        receiptViewFiltersDTO.getDebtPositionTypeOrgId(),
        receiptViewFiltersDTO.getPaymentDateTime().getFrom(),
        receiptViewFiltersDTO.getPaymentDateTime().getTo(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}


