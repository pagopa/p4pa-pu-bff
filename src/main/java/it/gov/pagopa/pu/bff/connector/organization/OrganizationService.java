package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;

public interface OrganizationService {

  Organization getOrganizationByIpaCode(String ipaCode, String accessToken);
}
