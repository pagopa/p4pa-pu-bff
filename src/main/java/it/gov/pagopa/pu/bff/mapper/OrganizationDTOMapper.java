package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Organization;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationDTOMapper {

  public OrganizationDTO mapToOrganizationDTO(Organization organization, List<String> roles) {
    return OrganizationDTO.builder()
      .organizationId(organization.getOrganizationId())
      .ipaCode(organization.getIpaCode())
      .orgName(organization.getOrgName())
      .operatorRole(roles)
      .orgLogo(organization.getOrgLogoDesc())
      .build();
  }

}
