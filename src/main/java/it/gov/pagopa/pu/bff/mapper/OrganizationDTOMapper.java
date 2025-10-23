package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OperatorRole;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrganizationDTOMapper {

  public OrganizationDTO mapToOrganizationDTO(Organization organization, List<String> roles) {
    OperatorRole operatorRole = determineOperatorRole(roles);

    return OrganizationDTO.builder()
      .organizationId(organization.getOrganizationId())
      .ipaCode(organization.getIpaCode())
      .orgName(organization.getOrgName())
      .operatorRole(operatorRole)
      .orgLogo(organization.getOrgLogo())
      .orgFiscalCode(organization.getOrgFiscalCode())
      .flagNotifyIo(organization.getFlagNotifyIo())
      .flagNotifyOutcomePush(organization.getFlagNotifyOutcomePush())
      .flagPaymentNotification(organization.getFlagPaymentNotification())
      .status(organization.getStatus())
      .brokerId(organization.getBrokerId())
      .build();
  }

  private OperatorRole determineOperatorRole(List<String> roles) {
    if (roles == null || roles.isEmpty()) {
      return null;
    }

    String operatorRoleValue = roles.stream()
      .filter("ROLE_ADMIN"::equals)
      .findFirst()
      .orElse(roles.getFirst());

    try {
      return OperatorRole.fromValue(operatorRoleValue);
    } catch (IllegalArgumentException e) {
      throw new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE: " + operatorRoleValue);
    }
  }

}
