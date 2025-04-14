package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrganizationRetrieverService {

  List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken);

  PagedOrganizationWithDebtPositionTypeOrgCount getOrganizationsWithDebtPositionTypeOrgCount(Long organizationId, String organizationName, Pageable pageable, UserInfo loggedUser, String accessToken);

}
