package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.p4paauth.controller.generated.AuthzApi;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

  private final AuthzApi authzApi;

  public OrganizationService(AuthzApi authzApi) {
    this.authzApi = authzApi;
  }

  public List<OrganizationDTO> getOrganizations(String mappedExternalUserId) {
    UserInfo userInfo = authzApi.getUserInfoFromMappedExternaUserId(mappedExternalUserId);

    if (userInfo == null) {
      return List.of();
    }

    return userInfo.getOrganizations().stream()
      .map(this::mapToOrganizationDTO)
      .toList();
  }

  private OrganizationDTO mapToOrganizationDTO(UserOrganizationRoles organization) {
    return OrganizationDTO.builder()
      .organizationId(null)
      .ipaCode(organization.getOrganizationIpaCode())
      .orgName("Organization " + organization.getOrganizationIpaCode())
      .operatorRole("Operator")
      .orgLogo(null)
      .build();
  }

}
