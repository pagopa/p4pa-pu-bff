package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.connector.OrganizationClient;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationClient organizationClient;

  public OrganizationServiceImpl(OrganizationClient organizationClient) {
    this.organizationClient = organizationClient;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo) {
    List<UserOrganizationRoles> userOrganizations = Optional.of(userInfo.getOrganizations())
      .orElse(Collections.emptyList());

    return userOrganizations.stream()
      .map(orgRoles -> {
        EntityModelOrganization organization =
          organizationClient.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode());
        return mapToOrganizationDTO(organization);
      }).toList();
  }

  private OrganizationDTO mapToOrganizationDTO(EntityModelOrganization organization) {
    return OrganizationDTO.builder()
      .organizationId(organization != null ? organization.getOrganizationId() : null)
      .ipaCode(organization != null ? organization.getIpaCode() : null)
      .orgName(organization != null ? organization.getOrgName() : null)
      .operatorRole("Operator")
      .build();
  }

}
