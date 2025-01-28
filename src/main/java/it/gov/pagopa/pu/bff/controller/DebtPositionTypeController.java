package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypesApi;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DebtPositionTypeController implements DebtPositionTypesApi {

  private final DebtPositionTypeService debtPositionTypeService;

  public DebtPositionTypeController(DebtPositionTypeService debtPositionTypeService) {
    this.debtPositionTypeService = debtPositionTypeService;
  }

  @Override
  public ResponseEntity<DebtPositionTypeDTO> getDebtPositionType(String id) {
    log.info("User requested getDebtPositionType()");
    return new ResponseEntity<>(debtPositionTypeService.getDebtPositionTypeById(SecurityUtils.getAccessToken(), Long.valueOf(id)), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeWithCount> getDebtPositionTypeWithCount(
    Long organizationId, Pageable pageable) {
    return ResponseEntity.ok(debtPositionTypeService.getDebtPositionTypeWithCount(
      organizationId, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
