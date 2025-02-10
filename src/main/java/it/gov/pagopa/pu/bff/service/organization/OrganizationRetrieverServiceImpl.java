package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrganizationRetrieverServiceImpl implements OrganizationRetrieverService {

  private final OrganizationService organizationService;

  private final OrganizationDTOMapper organizationDTOMapper;

  public OrganizationRetrieverServiceImpl(OrganizationService organizationService, OrganizationDTOMapper organizationDTOMapper) {
    this.organizationService = organizationService;
    this.organizationDTOMapper = organizationDTOMapper;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken) {
    return userInfo.getOrganizations().stream()
      .map(orgRoles -> Optional.ofNullable(
          organizationService.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(), accessToken))
        .map(organization -> organizationDTOMapper.mapToOrganizationDTO(organization, orgRoles.getRoles()))
        .orElse(null)
      ).filter(Objects::nonNull).toList();
  }

}
