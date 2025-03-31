package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

import java.util.List;

public interface OrganizationRetrieverService {

  List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken);

}
