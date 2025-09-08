package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetailsDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationRetrieverService {

  List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken);

  PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount getOrganizationsByBrokerId(UserInfo userInfo, Pageable pageable, String accessToken);

  PagedOrganizationWithDebtPositionTypeOrgCount getOrganizationsWithDebtPositionTypeOrgCount(Long organizationId, String organizationName, Pageable pageable, UserInfo loggedUser, String accessToken);

  String getOrgFiscalCode(Long organizationId, UserInfo loggedUser, String accessToken);

  void updateOrganization(Long organizationId, OrganizationDetailDTO organizationDetailDTO, UserInfo loggedUser, String accessToken);

  OrganizationDetailsDTO getOrganizationDetail(Long organizationId, UserInfo loggedUser, String accessToken);
}
