package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ReceiptsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.receipts.ReceiptViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Slf4j
@RestController
public class ReceiptViewController implements ReceiptsApi {

  private final ReceiptViewService receiptViewService;

  public ReceiptViewController(ReceiptViewService receiptViewService) {
    this.receiptViewService = receiptViewService;
  }

  @Override
  public ResponseEntity<PagedReceiptView> getReceipts(
    Long organizationId,
    String receiptOrigin,
    String operatorExternalUserId,
    String iuv,
    String iur,
    String iud,
    Long debtPositionTypeOrgId,
    OffsetDateTime fromDate,
    OffsetDateTime toDate,
    Pageable pageable) {
    return ResponseEntity.ok(receiptViewService.getReceipts(organizationId, receiptOrigin, operatorExternalUserId, iuv, iur, iud, debtPositionTypeOrgId, fromDate, toDate,
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
