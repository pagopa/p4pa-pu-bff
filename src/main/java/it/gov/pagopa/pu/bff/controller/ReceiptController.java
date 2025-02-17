package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.controller.generated.ReceiptsApi;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptController implements ReceiptsApi {

  private final ReceiptRetrieverService receiptRetrieverService;

  public ReceiptController(ReceiptRetrieverService receiptRetrieverService) {
    this.receiptRetrieverService = receiptRetrieverService;
  }

  @Override
  public ResponseEntity<PagedReceiptView> getReceipts(Long organizationId, ReceiptView.ReceiptOriginEnum receiptOrigin, String iuv, String iur, String iud, Long debtPositionTypeOrgId, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo, Pageable pageable) {
    UserInfo userInfo = SecurityUtils.getLoggedUser();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    return ResponseEntity.ok(receiptRetrieverService.getReceipts(
      new ReceiptViewFiltersDTO(organizationId, receiptOrigin, userInfo.getMappedExternalUserId(), iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter),
      pageable, userInfo, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ReceiptDetailDTO> getReceiptDetail(Long organizationId, Long receiptId) {
    return ResponseEntity.ofNullable(receiptRetrieverService.getReceiptDetail(organizationId, receiptId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
