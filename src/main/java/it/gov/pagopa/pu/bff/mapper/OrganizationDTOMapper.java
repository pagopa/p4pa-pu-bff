package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Organization;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationDTOMapper {

  public OrganizationDTO mapToOrganizationDTO(Organization organization, List<String> roles) {

    String operatorRoleValue = (roles != null && !roles.isEmpty()) ? roles.get(0) : null;

    OrganizationDTO.OperatorRoleEnum operatorRole = null;
    if (operatorRoleValue != null) {
      try {
        operatorRole = OrganizationDTO.OperatorRoleEnum.fromValue(operatorRoleValue);
      } catch (IllegalArgumentException e) {
        throw new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE: " + operatorRoleValue);
      }
    }

    return OrganizationDTO.builder()
      .organizationId(organization.getOrganizationId())
      .ipaCode(organization.getIpaCode())
      .orgName(organization.getOrgName())
      .operatorRole(operatorRole)
      .orgLogo(organization.getOrgLogoDesc())
      .build();
  }

}
