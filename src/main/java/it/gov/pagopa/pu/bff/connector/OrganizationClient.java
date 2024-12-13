package it.gov.pagopa.pu.bff.connector;


import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;

public interface OrganizationClient {

  EntityModelBroker getBrokerById(Long id);

  EntityModelOrganization getOrganizationByIpaCode(String ipaCode);

}
