package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.model.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class BrokerServiceImpl implements BrokerService{

  private OrganizationClientImpl organizationClient;

  private DefaultConfigFe defaultConfigFe;

  public BrokerServiceImpl(OrganizationClientImpl organizationClient,
      DefaultConfigFe defaultConfigFe){
    this.organizationClient = organizationClient;
    this.defaultConfigFe = defaultConfigFe;
  }

  @Override
  public PersonalisationFe getBrokerConfig(){
    UserInfo user = SecurityUtils.getLoggedUser();
    UserOrganizationRoles orgRoles = Optional.ofNullable(user.getOrganizations())
      .flatMap(orgs -> orgs.stream().findFirst())
      .orElse(null);

    EntityModelOrganization organization = null;
    EntityModelBroker broker = null;

    if (orgRoles != null && StringUtils.isNotBlank(orgRoles.getOrganizationIpaCode())) {
      organization = organizationClient.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode());
    }
    if(organization!=null && organization.getBrokerId()!=null ){
      broker = organizationClient.getBrokerById(String.valueOf(organization.getBrokerId()));
    }

    return getFEConfiguration(broker);
  }

  public PersonalisationFe getFEConfiguration(EntityModelBroker broker) {
    if (broker != null ) {
      return broker.getPersonalisationFe();
    } else {
      return this.defaultConfigFe;
    }
  }

}
