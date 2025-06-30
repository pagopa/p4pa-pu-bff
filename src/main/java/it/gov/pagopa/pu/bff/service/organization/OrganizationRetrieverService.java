package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationRetrieverService {

  List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken);

  PagedOrganizationWithDebtPositionTypeOrgCount getOrganizationsWithDebtPositionTypeOrgCount(Long organizationId, String organizationName, Pageable pageable, UserInfo loggedUser, String accessToken);

  String getOrgFiscalCode(Long organizationId, UserInfo loggedUser, String accessToken);
}
