package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;

import java.util.List;

public interface OrganizationService {

  List<OrganizationDTO> getOrganizations(UserInfo userInfo);

}
