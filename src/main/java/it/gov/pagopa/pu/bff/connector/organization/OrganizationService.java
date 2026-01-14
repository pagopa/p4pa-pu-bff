package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface OrganizationService {

  Organization getOrganizationByIpaCode(String ipaCode, String accessToken);

  PagedModelOrganization getOrganizationByBrokerIdAndOrgName(Long brokerId, String orgName, Pageable pageable, String accessToken);

  Organization getOrganizationByOrganizationId(Long organizationId, String accessToken);

  PagedModelOrganization getOrganizationsByBrokerIdAndFilters(Long brokerId, String orgName, String ipaCode, String orgFiscalCode, Set<Long> allowedOrganizationIds, Pageable pageable, String accessToken);

  void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken);

  OrganizationDetailDTO getOrganizationDetail(Long organizationId, String accessToken);
}
