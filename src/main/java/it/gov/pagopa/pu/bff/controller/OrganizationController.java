package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.organization.OrganizationServiceImpl;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrganizationController {

  private final OrganizationServiceImpl organizationService;

  public OrganizationController(OrganizationServiceImpl organizationService) {
    this.organizationService = organizationService;
  }

  @GetMapping(path = "getOrganizations")
  public List<OrganizationDTO> organizations() {
    UserInfo userInfo = SecurityUtils.getLoggedUser();
    if (userInfo == null) {
      return List.of();
    }
    return organizationService.getOrganizations(userInfo);
  }

}
