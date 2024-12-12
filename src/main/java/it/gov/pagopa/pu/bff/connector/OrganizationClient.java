package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelOrganization;

public interface OrganizationClient {

  EntityModelBroker getBrokerById(String id);

  EntityModelOrganization getOrganizationByIpaCode(String ipaCode);

}
