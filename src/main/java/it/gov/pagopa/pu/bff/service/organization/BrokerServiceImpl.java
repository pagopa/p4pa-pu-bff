package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class BrokerServiceImpl implements BrokerService{

  private OrganizationClientImpl organizationClient;

  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;

  private DefaultConfigFe defaultConfigFe;

  public BrokerServiceImpl(OrganizationClientImpl organizationClient,
      DefaultConfigFe defaultConfigFe,
      PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper){
    this.organizationClient = organizationClient;
    this.defaultConfigFe = defaultConfigFe;
    this.personalisationFE2ConfigFEMapper = personalisationFE2ConfigFEMapper;
  }

  @Override
  public ConfigFE getBrokerConfig(UserInfo user, String accessToken){
    UserOrganizationRoles orgRoles = Optional.ofNullable(user.getOrganizations())
      .flatMap(orgs -> orgs.stream().findFirst())
      .orElse(null);

    EntityModelOrganization organization = null;
    EntityModelBroker broker = null;

    if (orgRoles != null && StringUtils.isNotBlank(orgRoles.getOrganizationIpaCode())) {
      organization = organizationClient.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(),accessToken);
    }
    if(organization!=null && organization.getBrokerId()!=null ){
      broker = organizationClient.getBrokerById(organization.getBrokerId(),accessToken);
    }
    PersonalisationFe personalisationFe = getFEConfiguration(broker);
    return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(personalisationFe);
  }

  public PersonalisationFe getFEConfiguration(EntityModelBroker broker) {
    if (broker != null ) {
      return broker.getPersonalisationFe();
    } else {
      return this.defaultConfigFe;
    }
  }

}
