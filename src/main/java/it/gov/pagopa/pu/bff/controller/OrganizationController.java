package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrganizationsApi;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrganizationController implements OrganizationsApi {

  private final OrganizationRetrieverService organizationRetrieverService;

  public OrganizationController(OrganizationRetrieverService organizationRetrieverService) {
    this.organizationRetrieverService = organizationRetrieverService;
  }

  @Override
  public ResponseEntity<List<OrganizationDTO>> getOrganizations() {
    log.info("User requested getOrganizations()");
    return new ResponseEntity<>(organizationRetrieverService.getOrganizations(SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedOrganizationWithDebtPositionTypeOrgCount> getOrganizationsWithDebtPositionTypeOrgCount(Long organizationId, String organizationName, Pageable pageable) {
    log.info("User requested getOrganizationWithDebtPositionTypeOrgCount");
    return ResponseEntity.ofNullable(organizationRetrieverService.getOrganizationsWithDebtPositionTypeOrgCount(organizationId, organizationName, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
