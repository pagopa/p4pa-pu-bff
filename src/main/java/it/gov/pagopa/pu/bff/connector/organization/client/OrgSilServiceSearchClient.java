package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrgSilServiceSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrgSilServiceSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken) {
    return organizationApisHolder.getOrgSilServiceSearchControllerApi(accessToken)
      .crudOrgSilServicesFindAllByOrganizationIdAndServiceType(organizationId,serviceType);
  }

  public OrgSilService getOrgSilServiceById(Long orgSilServiceId, String accessToken) {
    try {
      return organizationApisHolder.getOrgSilServiceEntityControllerApi(accessToken)
        .crudGetOrgsilservice(String.valueOf(orgSilServiceId));
    } catch (HttpClientErrorException.NotFound e) {
      log.info("OrgSilService with ID {} not found", orgSilServiceId);
      return null;
    }
  }

  public PagedModelOrgSilServiceView getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable, String accessToken) {
    return organizationApisHolder.getOrgSilServiceViewSearchControllerApi(accessToken)
      .crudOrgSilServicesViewFindOrgSilServicesByFilters(
        organizationId,
        applicationName,
        serviceType,
        flagLegacy,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
  public OrgSilServiceDTO getOrgSilServiceByIdDecrypted(Long orgSilServiceId, String accessToken){
    try {
      return organizationApisHolder.getOrganizationSilServiceApi(accessToken).getOrgSilService(orgSilServiceId);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("OrgSilService with ID {} not found", orgSilServiceId);
      return null;
    }

  }

}
