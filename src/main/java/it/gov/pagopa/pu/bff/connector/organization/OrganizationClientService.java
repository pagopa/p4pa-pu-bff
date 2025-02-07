package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;

public interface OrganizationClientService {

  Organization getOrganizationByIpaCode(String ipaCode, String accessToken);
}
