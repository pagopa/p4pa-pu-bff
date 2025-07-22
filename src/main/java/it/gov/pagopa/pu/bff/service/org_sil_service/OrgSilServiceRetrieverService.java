package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTOExtended;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtended;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgSilServiceRetrieverService {
  List<OrgSilServiceExtended> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, UserInfo loggedUser, String accessToken);
  OrgSilServiceDTOExtended getOrgSilServiceDetails(Long organizationId, Long orgSilServiceId, UserInfo loggedUser, String accessToken);
  String getOrgSilServiceApplicationName(Long serviceId, String accessToken);

  PagedOrgSilServiceView getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable, UserInfo loggedUser, String accessToken);
}
