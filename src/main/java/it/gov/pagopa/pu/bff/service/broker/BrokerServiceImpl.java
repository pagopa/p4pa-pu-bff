package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrokerServiceImpl implements BrokerService {

  private OrganizationClientImpl organizationClient;

  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;

  private DefaultConfigFe defaultConfigFe;

  private final ConfigFE defaultFEConfig;

  public BrokerServiceImpl(OrganizationClientImpl organizationClient,
                           DefaultConfigFe defaultConfigFe,
                           PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper) {
    this.organizationClient = organizationClient;
    this.defaultConfigFe = defaultConfigFe;
    this.personalisationFE2ConfigFEMapper = personalisationFE2ConfigFEMapper;
    this.defaultFEConfig = getFEConfiguration(null);
  }

  @Override
  public ConfigFE getBrokerConfig(UserInfo user, String accessToken) {
    String orgIpaCode = Optional.ofNullable(user.getOrganizationAccess())
      .orElseGet(() -> user.getOrganizations().stream()
        .findFirst()
        .map(UserOrganizationRoles::getOrganizationIpaCode)
        .orElse(null)
      );

    if (StringUtils.isNotBlank(orgIpaCode)) {
      EntityModelOrganization organization = organizationClient.getOrganizationByIpaCode(orgIpaCode, accessToken);
      if (organization != null && organization.getBrokerId() != null) {
        return getFEConfiguration(organizationClient.getBrokerById(organization.getBrokerId(), accessToken));
      }
    }
    return this.defaultFEConfig;
  }

  public ConfigFE getFEConfiguration(EntityModelBroker broker) {
    if (broker != null) {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(broker.getPersonalisationFe());
    } else {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(this.defaultConfigFe);
    }
  }

}
