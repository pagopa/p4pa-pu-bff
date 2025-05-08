package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypeOrgsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DebtPositionTypeOrgController implements DebtPositionTypeOrgsApi {

  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;

  public DebtPositionTypeOrgController(
    DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService) {
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
  }

  @Override
  public ResponseEntity<DebtPositionTypeOrg> getDebtPositionTypeOrgById(Long organizationId, Long debtPositionTypeOrgId) {
    log.info("User requested getDebtPositionTypeOrgById having organizationId {} and debtPositionTypeOrgId {}", organizationId, debtPositionTypeOrgId);
    DebtPositionTypeOrg result = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    if (result == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<List<DebtPositionTypeOrg>> getDebtPositionTypeOrgs(Long organizationId) {
    log.info("User requested getDebtPositionTypeOrgs having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgs(
      organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeOrgWithCount> getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable) {
    log.info("User requested getDebtPositionTypeOrgWithCount having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgWithCount(
      organizationId, code, description, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteDebtPositionTypeOrg(Long organizationId,
    Long debtPositionTypeOrgId) {
    log.info("User requested deleteDebtPositionTypeOrg having organizationId {} and debtPositionTypeOrgId {}", organizationId, debtPositionTypeOrgId);
    debtPositionTypeOrgRetrieverService.deleteDebtPositionTypeOrg(
      organizationId, debtPositionTypeOrgId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeOrgOperatorDTO> getDebtPositionTypeOrgOperators(Long organizationId, Long debtPositionTypeOrgId, Pageable pageable) {
    log.info("User requested getDebtPositionTypeOrgOperators having organizationId {} and debtPositionTypeOrgId {}", organizationId, debtPositionTypeOrgId);
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgOperators(organizationId, debtPositionTypeOrgId, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<DebtPositionTypeOrg> createDebtPositionTypeOrg(Long organizationId, SaveDebtPositionTypeOrgDTO createDebtPositionTypeOrgDTO) {
    log.info("User requested createDebtPositionTypeOrg having organizationId {}", organizationId);
    return new ResponseEntity<>(debtPositionTypeOrgRetrieverService.createDebtPositionTypeOrg(organizationId,createDebtPositionTypeOrgDTO,
      SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()),HttpStatus.CREATED);
  }
}
