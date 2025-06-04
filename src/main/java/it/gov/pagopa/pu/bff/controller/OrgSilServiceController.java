package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrgSilServiceApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class OrgSilServiceController implements OrgSilServiceApi {

  private final OrgSilServiceRetrieverService orgSilServiceRetrieverService;

  public OrgSilServiceController(OrgSilServiceRetrieverService orgSilServiceRetrieverService) {
    this.orgSilServiceRetrieverService = orgSilServiceRetrieverService;
  }

  @Override
  public ResponseEntity<List<OrgSilService>> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType) {
    log.info("User requested getOrgSilServices having organizationId {} and serviceType {}",organizationId,serviceType);
    return ResponseEntity.ok(orgSilServiceRetrieverService.getOrgSilServices(organizationId,serviceType,
            SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
