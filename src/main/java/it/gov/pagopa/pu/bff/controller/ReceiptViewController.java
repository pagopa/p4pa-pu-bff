package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ReceiptsApi;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.receipts.ReceiptViewService;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class ReceiptViewController implements ReceiptsApi {

  private final ReceiptViewService receiptViewService;

  public ReceiptViewController(ReceiptViewService receiptViewService) {
    this.receiptViewService = receiptViewService;
  }

  @Override
  public ResponseEntity<PagedReceiptView> getReceipts(Long organizationId, ReceiptView.ReceiptOriginEnum receiptOrigin, String operatorExternalUserId, String iuv, String iur, String iud, Long debtPositionTypeOrgId, OffsetDateTime fromDate, OffsetDateTime toDate, Pageable pageable) {
    return ResponseEntity.ok(receiptViewService.getReceipts(new ReceiptViewFiltersDTO(organizationId, receiptOrigin, operatorExternalUserId, iuv, iur, iud, debtPositionTypeOrgId, fromDate, toDate),
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
