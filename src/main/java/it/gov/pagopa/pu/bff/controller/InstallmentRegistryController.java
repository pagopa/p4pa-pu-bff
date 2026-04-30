package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.InstallmentRegistryApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.installment_registry.InstallmentRegistryRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.InstallmentRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class InstallmentRegistryController implements InstallmentRegistryApi {
  private final InstallmentRegistryRetrieverService installmentRegistryRetrieverService;

  public InstallmentRegistryController(InstallmentRegistryRetrieverService installmentRegistryRetrieverService) {
    this.installmentRegistryRetrieverService = installmentRegistryRetrieverService;
  }

  @Override
  public ResponseEntity<List<InstallmentRegistry>> getInstallmentRegistries(Long organizationId, Long debtPositionId, String nav) {
    log.info("User requested getInstallmentRegistries having organizationId {} and debtPositionId {} and nav {}", organizationId, debtPositionId, nav);
    return ResponseEntity.ok(installmentRegistryRetrieverService.getInstallmentRegistries(
      organizationId, debtPositionId, nav, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
