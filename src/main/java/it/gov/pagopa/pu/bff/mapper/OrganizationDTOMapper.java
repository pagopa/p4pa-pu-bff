package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationDTOMapper {

  public OrganizationDTO mapToOrganizationDTO(EntityModelOrganization organization, List<String> roles) {
    return OrganizationDTO.builder()
      .organizationId(organization != null ? organization.getOrganizationId() : null)
      .ipaCode(organization != null ? organization.getIpaCode() : null)
      .orgName(organization != null ? organization.getOrgName() : null)
      .operatorRole((roles != null && !roles.isEmpty()) ? roles.get(0) : null)
      .build();
  }

}
