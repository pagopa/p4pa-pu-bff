package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
        receiptViewFiltersDTO.getOrganizationId(),
        receiptViewFiltersDTO.getOperatorExternalUserId(),
        receiptViewFiltersDTO.getReceiptOrigins(),
        receiptViewFiltersDTO.getIuv(),
        receiptViewFiltersDTO.getIur(),
        receiptViewFiltersDTO.getIud(),
        receiptViewFiltersDTO.getDebtPositionTypeOrgId(),
        receiptViewFiltersDTO.getPaymentDateTime().getFrom(),
        receiptViewFiltersDTO.getPaymentDateTime().getTo(),
        receiptViewFiltersDTO.getFiscalCode(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public ReceiptDetailDTO getReceiptDetail(Long receiptId, String operatorExternalUserId, Long organizationId, String iud, String accessToken) {
    try {
      return debtPositionApisHolder.getReceiptApi(accessToken)
        .getReceiptDetail(receiptId, organizationId, operatorExternalUserId, iud);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("ReceiptDetail with receiptId {} and operatorExternalUserId {} not found", receiptId, operatorExternalUserId);
      return null;
    }
  }

  public FileResourceDTO getReceiptPdf(Long receiptId, Long organizationId, String accessToken) {
    try {
      ResponseEntity<Resource> resourceResponseEntity= debtPositionApisHolder.getReceiptApi(accessToken)
        .getReceiptPdfWithHttpInfo(receiptId, organizationId);
      return FileResourceDTO.builder()
        .resource(resourceResponseEntity.getBody())
        .fileName(resourceResponseEntity.getHeaders().getContentDisposition().getFilename())
        .build();
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Receipt PDF with receiptId {} and organizationId {} not found", receiptId, organizationId);
      return null;
    }
  }
}


