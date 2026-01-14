package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrganizationsApi;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    return ResponseEntity.ok(organizationRetrieverService.getOrganizationsWithDebtPositionTypeOrgCount(organizationId, organizationName, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount> getOrganizationsByBrokerIdAndFilters(String orgName, String ipaCode, String orgFiscalCode, Pageable pageable) {
    log.info("User requested getOrganizationsByBrokerIdAndFilters()");
    return new ResponseEntity<>(organizationRetrieverService.getOrganizationsByBrokerIdAndFilters(SecurityUtils.getLoggedUser(), orgName, ipaCode, orgFiscalCode, pageable, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> updateOrganization(Long organizationId, OrganizationDetailDTO organizationDetailDTO) {
    log.info("User requested updateOrganization having organizationId {}", organizationId);
    organizationRetrieverService.updateOrganization(organizationId, organizationDetailDTO,SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<OrganizationDetail> getOrganizationDetail(Long organizationId) {
    log.info("User requested getOrganizationDetail having organizationId {}", organizationId);
    return ResponseEntity.ok(organizationRetrieverService.getOrganizationDetail(organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
