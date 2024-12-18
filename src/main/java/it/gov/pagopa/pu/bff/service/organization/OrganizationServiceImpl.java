package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.connector.OrganizationClient;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationClient organizationClient;

  private final OrganizationDTOMapper organizationDTOMapper;

  public OrganizationServiceImpl(OrganizationClient organizationClient, OrganizationDTOMapper organizationDTOMapper) {
    this.organizationClient = organizationClient;
    this.organizationDTOMapper = organizationDTOMapper;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken) {
    return userInfo.getOrganizations().stream()
      .map(orgRoles -> {
        EntityModelOrganization organization = organizationClient.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(), accessToken);
        if (organization != null) {
          return organizationDTOMapper.mapToOrganizationDTO(organization, orgRoles.getRoles());
        }
        return null;
      }).filter(Objects::nonNull).toList();
  }

}
