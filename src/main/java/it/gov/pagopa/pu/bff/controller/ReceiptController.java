package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.ReceiptService;
import it.gov.pagopa.pu.bff.controller.generated.ReceiptsApi;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
public class ReceiptController implements ReceiptsApi {

  private final ReceiptRetrieverService receiptRetrieverService;
  private final ReceiptService receiptService;

  public ReceiptController(ReceiptRetrieverService receiptRetrieverService, ReceiptService receiptService) {
    this.receiptRetrieverService = receiptRetrieverService;
    this.receiptService = receiptService;
  }

  @Override
  public ResponseEntity<PagedReceiptView> getReceipts(Long organizationId, List<ReceiptOriginType> receiptOrigins, String iuv, String iur, String iud, Long debtPositionTypeOrgId, String fiscalCode, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo, Pageable pageable) {
    log.info("User requested getReceipts having organizationId {} and receiptOrigins {}", organizationId, receiptOrigins);
    UserInfo userInfo = SecurityUtils.getLoggedUser();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    return ResponseEntity.ok(receiptRetrieverService.getReceipts(
      new ReceiptViewFiltersDTO(organizationId, receiptOrigins, userInfo.getMappedExternalUserId(), iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter, fiscalCode),
      pageable, userInfo, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ReceiptDetailDTO> getReceiptDetail(Long organizationId, Long receiptId) {
    log.info("User requested getReceiptDetail having organizationId {} and receiptId {}", organizationId, receiptId);
    return ResponseEntity.ofNullable(receiptRetrieverService.getReceiptDetail(organizationId, receiptId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Resource> getReceiptPdf(Long organizationId, Long receiptId) {
    log.info("User requested getReceiptPdf having organizationId {} and receiptId {}", organizationId, receiptId);
    FileResourceDTO resource= receiptService.getReceiptPdf(receiptId, organizationId, SecurityUtils.getAccessToken());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(ContentDisposition.attachment()
            .filename(resource.getFileName())
            .build());
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .headers(headers)
            .body(resource.getResource());
  }
}
