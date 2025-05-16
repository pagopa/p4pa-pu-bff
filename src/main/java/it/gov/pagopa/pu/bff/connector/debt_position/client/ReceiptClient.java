package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
        receiptViewFiltersDTO.getOperatorExternalUserId(),
        receiptViewFiltersDTO.getReceiptOrigin(),
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

  public ReceiptDetailDTO getReceiptDetail(Long receiptId, String operatorExternalUserId, String accessToken) {
    try {
      return debtPositionApisHolder.getReceiptApi(accessToken)
        .getReceiptDetail(receiptId, operatorExternalUserId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("ReceiptDetail with receiptId {} and operatorExternalUserId {} not found", receiptId, operatorExternalUserId);
      return null;
    }
  }

}


