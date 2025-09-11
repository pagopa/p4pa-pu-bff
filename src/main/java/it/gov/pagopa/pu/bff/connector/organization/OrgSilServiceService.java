package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;

public interface OrgSilServiceService {
  CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken);

  OrgSilService getOrgSilServiceById(Long orgSilServiceId, String accessToken);

  PagedModelOrgSilServiceView getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable, String accessToken);

  OrgSilServiceDTO getOrgSilServiceByIdDecrypted(Long orgSilServiceId, String accessToken);

  OrgSilServiceDTO createOrUpdateOrgSilService(OrgSilServiceDTO orgSilServiceDTO, String accessToken);

  void deleteOrgSilService(Long orgSilServiceId, String accessToken);
  OrgSilService getOrgSilServiceByOrganizationIdAndApplicationName(Long organizationId, String applicationName, String accessToken);
}
