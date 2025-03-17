package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypesApi;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DebtPositionTypeController implements DebtPositionTypesApi {

  private final DebtPositionTypeRetrieverService debtPositionTypeRetrieverService;

  public DebtPositionTypeController(DebtPositionTypeRetrieverService debtPositionTypeRetrieverService) {
    this.debtPositionTypeRetrieverService = debtPositionTypeRetrieverService;
  }

  @Override
  public ResponseEntity<DebtPositionType> getDebtPositionType(String id) {
    log.info("User requested getDebtPositionType()");
    return new ResponseEntity<>(debtPositionTypeRetrieverService.getDebtPositionTypeById(SecurityUtils.getAccessToken(), Long.valueOf(id)), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeWithCount> getDebtPositionTypeWithCount(
    Long organizationId, Pageable pageable) {
    log.info("User requested getDebtPositionTypeWithCount having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeRetrieverService.getDebtPositionTypeWithCount(
      organizationId, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<DebtPositionTypeDetailDTO> getDebtPositionTypeDetail(
    Long organizationId, Long debtPositionTypeId) {
    log.info(
      "User requested getDebtPositionDetail having organizationId {} and ID {}",
      organizationId, debtPositionTypeId);
    return ResponseEntity.ofNullable(
      debtPositionTypeRetrieverService.getDebtPositionTypeDetail(organizationId,
        debtPositionTypeId, SecurityUtils.getLoggedUser(),
        SecurityUtils.getAccessToken()));
  }
}
