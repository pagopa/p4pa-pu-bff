package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DashboardApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.dashboard.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DashboardController implements DashboardApi {

  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @Override
  public ResponseEntity<PagedDashboardDTO> getInstallmentsDashboardByFiscalCode(
    Long organizationId, String fiscalCode) {
    log.info("User requested getInstallmentDashboardByFiscalCode with organizationId {}", organizationId);

    return ResponseEntity.ofNullable(
      dashboardService.getInstallmentsByFiscalCode(organizationId, fiscalCode,
        SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
