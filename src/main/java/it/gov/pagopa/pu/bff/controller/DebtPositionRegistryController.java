package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionRegistryApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_registry.DebtPositionRegistryRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class DebtPositionRegistryController implements DebtPositionRegistryApi {
  private final DebtPositionRegistryRetrieverService debtPositionRegistryRetrieverService;

    public DebtPositionRegistryController(DebtPositionRegistryRetrieverService debtPositionRegistryRetrieverService) {
        this.debtPositionRegistryRetrieverService = debtPositionRegistryRetrieverService;
    }

  @Override
  public ResponseEntity<List<DebtPositionRegistry>> getDebtPositionRegistries(Long organizationId, Long debtPositionId) {
    log.info("User requested getDebtPositionRegistries having organizationId {} and debtPositionId {}", organizationId, debtPositionId);
    return ResponseEntity.ok(debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId,debtPositionId,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
