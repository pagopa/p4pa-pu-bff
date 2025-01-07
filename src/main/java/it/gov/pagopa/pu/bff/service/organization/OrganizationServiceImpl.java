package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationSearchClient organizationSearchClient;

  private final OrganizationDTOMapper organizationDTOMapper;

  public OrganizationServiceImpl(OrganizationSearchClient organizationSearchClient, OrganizationDTOMapper organizationDTOMapper) {
    this.organizationSearchClient = organizationSearchClient;
    this.organizationDTOMapper = organizationDTOMapper;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken) {
    return userInfo.getOrganizations().stream()
      .map(orgRoles -> Optional.ofNullable(
          organizationSearchClient.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(), accessToken))
        .map(organization -> organizationDTOMapper.mapToOrganizationDTO(organization, orgRoles.getRoles()))
        .orElse(null)
      ).filter(Objects::nonNull).toList();
  }

}
