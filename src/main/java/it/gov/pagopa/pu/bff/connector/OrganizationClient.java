package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.model.generated.PagedModelEntityModelBroker;

public interface OrganizationClient {

  PagedModelEntityModelBroker getBrokerConfig();

  EntityModelOrganization getOrganizationByIpaCode(String ipaCode);

}
