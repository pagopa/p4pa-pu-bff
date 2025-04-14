package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionsApi;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;


@Slf4j
@RestController
public class DebtPositionController implements DebtPositionsApi {

  private final DebtPositionRetrieverService debtPositionRetrieverService;

  public DebtPositionController(
    DebtPositionRetrieverService debtPositionRetrieverService) {
    this.debtPositionRetrieverService = debtPositionRetrieverService;
  }

  @Override
  public ResponseEntity<DebtPositionDTO> createDebtPosition(DebtPositionDTO debtPositionDTO, Boolean massive) {
    log.info("User requested createDebtPosition having organizationId {}", debtPositionDTO.getOrganizationId());

    return ResponseEntity.ok(debtPositionRetrieverService.createDebtPosition(debtPositionDTO, massive, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
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
}
