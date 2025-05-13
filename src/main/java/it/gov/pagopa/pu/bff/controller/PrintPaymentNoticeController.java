package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PrintPaymentNoticeApi;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverService;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PrintPaymentNoticeController implements PrintPaymentNoticeApi {

  private final PrintPaymentNoticeRetrieverService printPaymentNoticeRetrieverService;

  public PrintPaymentNoticeController(PrintPaymentNoticeRetrieverService printPaymentNoticeRetrieverService) {
    this.printPaymentNoticeRetrieverService = printPaymentNoticeRetrieverService;
  }

  @Override
  public ResponseEntity<Resource> generateNotice(Long organizationId,
    String iuv, DebtPositionDTO debtPositionDTO) {
    log.info("User requested generateNotice having organizationId {} and iuv {}", organizationId, iuv);

    FileResourceDTO fileResourceDTO = printPaymentNoticeRetrieverService.generateNotice(
      organizationId,iuv,debtPositionDTO,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(ContentDisposition.attachment()
      .filename(fileResourceDTO.getFileName())
      .build());

    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_PDF)
      .headers(headers)
      .body(fileResourceDTO.getResource());
  }
}
