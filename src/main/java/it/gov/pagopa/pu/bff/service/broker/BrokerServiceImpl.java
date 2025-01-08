package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Organization;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserOrganizationRoles;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrokerServiceImpl implements BrokerService {

  private final OrganizationSearchClient organizationSearchClient;
  private final BrokerEntityClient brokerEntityClient;
  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;
  private final DefaultConfigFe defaultConfigFe;
  private final ConfigFE defaultFEConfig;

  public BrokerServiceImpl(OrganizationSearchClient organizationSearchClient, BrokerEntityClient brokerEntityClient,
                           DefaultConfigFe defaultConfigFe,
                           PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper) {
    this.organizationSearchClient = organizationSearchClient;
    this.brokerEntityClient = brokerEntityClient;
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
      Organization organization = organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken);
      if (organization != null && organization.getBrokerId() != null) {
        return getFEConfiguration(brokerEntityClient.getBrokerById(organization.getBrokerId(), accessToken));
      }
    }
    return this.defaultFEConfig;
  }

  public ConfigFE getFEConfiguration(Broker broker) {
    if (broker != null) {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(broker.getPersonalisationFe());
    } else {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(this.defaultConfigFe);
    }
  }

}
