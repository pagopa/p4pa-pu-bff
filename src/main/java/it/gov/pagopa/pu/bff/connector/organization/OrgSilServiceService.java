package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;

public interface OrgSilServiceService {
  CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken);

  OrgSilService getOrgSilServiceById(Long orgSilServiceId, String accessToken);
}
