package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrganizationsApi;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.organization.OrganizationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
public class OrganizationController implements OrganizationsApi {

  private final OrganizationService organizationService;

  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @Override
  public ResponseEntity<List<OrganizationDTO>> getOrganizations() {
    log.info("User requested getOrganizations()");
    return new ResponseEntity<>(organizationService.getOrganizations(SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

}
