package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrgSilServiceApi;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceStorerService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class OrgSilServiceController implements OrgSilServiceApi {

  private final OrgSilServiceRetrieverService orgSilServiceRetrieverService;
  private final OrgSilServiceStorerService orgSilServiceStorerService;

  public OrgSilServiceController(OrgSilServiceRetrieverService orgSilServiceRetrieverService, OrgSilServiceStorerService orgSilServiceStorerService) {
    this.orgSilServiceRetrieverService = orgSilServiceRetrieverService;
    this.orgSilServiceStorerService = orgSilServiceStorerService;
  }

  @Override
  public ResponseEntity<List<OrgSilServiceExtendedDTO>> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType) {
    log.info("User requested getOrgSilServices having organizationId {} and serviceType {}", organizationId, serviceType);
    return ResponseEntity.ok(orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType,
      SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedOrgSilServiceView> getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable) {
    log.info("User requested getOrgSilServicesByFilters having organizationId {}", organizationId);
    return ResponseEntity.ok(orgSilServiceRetrieverService.getOrgSilServicesByFilters(
      organizationId, applicationName, serviceType, flagLegacy, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<OrgSilServiceDecryptedDTO> getOrgSilServiceDetails(Long organizationId, Long orgSilServiceId) {
    log.info("User requested getOrgSilServiceDetails having organizationId {} and orgSilServiceId {}", organizationId, orgSilServiceId);
    return ResponseEntity.ofNullable(orgSilServiceRetrieverService.getOrgSilServiceDetails(organizationId, orgSilServiceId,
      SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<OrgSilServiceDecryptedDTO> createOrgSilService(Long organizationId, OrgSilServiceDecryptedDTO body) {
    log.info("User requested createOrgSilService having organizationId {} and applicationName {}", organizationId, body.getApplicationName());
    return new ResponseEntity<>(orgSilServiceStorerService.createOrgSilService(
      organizationId, body, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> deleteOrgSilService(Long organizationId, Long orgSilServiceId) {
    log.info("User requested deleteOrgSilService having organizationId {} and orgSilServiceId {}", organizationId, orgSilServiceId);
    orgSilServiceRetrieverService.deleteOrgSilService(organizationId, orgSilServiceId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }
}
