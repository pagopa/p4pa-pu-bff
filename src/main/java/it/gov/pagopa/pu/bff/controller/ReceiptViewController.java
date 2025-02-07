package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ReceiptsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.receipts.ReceiptViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReceiptViewController implements ReceiptsApi {

  private final ReceiptViewService receiptViewService;

  public ReceiptViewController(ReceiptViewService receiptViewService) {
    this.receiptViewService = receiptViewService;
  }

  @Override
  public ResponseEntity<PagedReceiptView> getReceipts(ReceiptFilterDTO filter, Pageable pageable) {
    return ResponseEntity.ok(receiptViewService.getReceipts(filter, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
