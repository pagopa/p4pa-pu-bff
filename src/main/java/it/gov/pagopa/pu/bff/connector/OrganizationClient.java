package it.gov.pagopa.pu.bff.connector;


import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;

public interface OrganizationClient {

  EntityModelOrganization getOrganizationByIpaCode(String ipaCode, String accessToken);

}
