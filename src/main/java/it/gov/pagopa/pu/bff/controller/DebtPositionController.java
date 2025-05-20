package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionsApi;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionNoticeRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;


@Slf4j
@RestController
public class DebtPositionController implements DebtPositionsApi {

  private final DebtPositionRetrieverService debtPositionRetrieverService;
  private final DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverService;

  public DebtPositionController(
    DebtPositionRetrieverService debtPositionRetrieverService,
    DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverService) {
    this.debtPositionRetrieverService = debtPositionRetrieverService;
    this.debtPositionNoticeRetrieverService = debtPositionNoticeRetrieverService;
  }

  @Override
  public ResponseEntity<DebtPositionDTO> createDebtPosition(DebtPositionDTO debtPositionDTO) {
    log.info("User requested createDebtPosition having organizationId {}", debtPositionDTO.getOrganizationId());

    return ResponseEntity.ok(debtPositionRetrieverService.createDebtPosition(debtPositionDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedDebtPositionView> getDebtPositionViews(
    Long organizationId,
    OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo,
    String fiscalCode,
    Long debtPositionTypeOrgId,
    DebtPositionStatus status,
    Pageable pageable) {
    log.info("User requested getDebtPositionViews having organizationId {} , creationDateFrom {} , creationDateTo {} ", organizationId, creationDateFrom, creationDateTo);
    return ResponseEntity.ok(debtPositionRetrieverService.getDebtPositionViews(
      new DebtPositionViewFiltersDTO(organizationId,
          creationDateFrom,
          creationDateTo,
          fiscalCode,
          debtPositionTypeOrgId,
          status),
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken())
    );
  }

  @Override
  public ResponseEntity<DebtPositionDetailDTO> getDebtPositionDetail(
    Long organizationId,
    Long debtPositionId) {
    log.info("User requested getDebtPositionDetail having organizationId {} and debtPositionId {} ", organizationId, debtPositionId);
    return ResponseEntity.ofNullable(debtPositionRetrieverService.getDebtPositionDetail(debtPositionId,organizationId,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteDebtPosition(Long organizationId, Long debtPositionId) {
    log.info("User requested deleteDebtPosition having organizationId {} and debtPositionId {} ", organizationId, debtPositionId);
    boolean deletedDebtPositionPhysically = debtPositionRetrieverService.deleteDebtPosition(organizationId, debtPositionId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    if (deletedDebtPositionPhysically){
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Resource> getPaymentNotice(Long organizationId,
    Long debtPositionId, String iuv) {
    log.info("User requested getPaymentNotice having organizationId {} debtPositionId {} and iuv {}", organizationId, debtPositionId, iuv);

    FileResourceDTO fileResourceDTO = debtPositionNoticeRetrieverService.getNotice(
      organizationId,iuv,debtPositionId,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(ContentDisposition.attachment()
      .filename(fileResourceDTO.getFileName())
      .build());

    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_PDF)
      .headers(headers)
      .body(fileResourceDTO.getResource());
  }

  @Override
  public ResponseEntity<Resource> getPaymentNoticeZip(Long organizationId, Long debtPositionId) {
    log.info("User requested getPaymentNoticeZip having organizationId {} and debtPositionId {} ", organizationId, debtPositionId);

    Resource debtPositionPaymentNoticesZipped = debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    if (debtPositionPaymentNoticesZipped != null){
      HttpHeaders headers = new HttpHeaders();
      headers.setContentDisposition(ContentDisposition.attachment()
        .filename(buildZipFileName(organizationId, debtPositionId))
        .build());

      return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(debtPositionPaymentNoticesZipped);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

  }

  private String buildZipFileName(Long organizationId, Long debtPositionId) {
    return organizationId + "_" + debtPositionId + "_NOTICES_PDF.zip";
  }
}
