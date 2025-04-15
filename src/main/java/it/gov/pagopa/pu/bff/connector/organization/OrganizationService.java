package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {

  Organization getOrganizationByIpaCode(String ipaCode, String accessToken);
  PagedModelOrganization getOrganizationByBrokerIdAndOrgName(String brokerId, String orgName, Pageable pageable, String accessToken);
  Organization getOrganizationByOrganizationId(Long organizationId, String accessToken);
}
