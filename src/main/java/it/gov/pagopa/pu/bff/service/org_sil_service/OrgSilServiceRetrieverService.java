package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;

import java.util.List;

public interface OrgSilServiceRetrieverService {
  List<OrgSilServiceDTO> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, UserInfo loggedUser, String accessToken);

  String getOrgSilServiceApplicationName(Long serviceId, String accessToken);
}
