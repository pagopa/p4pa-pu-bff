package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrokerServiceImpl implements BrokerService {

  private final BrokerEntityClient brokerEntityClient;
  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;
  private final DefaultConfigFe defaultConfigFe;

  public BrokerServiceImpl(BrokerEntityClient brokerEntityClient,
                           DefaultConfigFe defaultConfigFe,
                           PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper) {
    this.brokerEntityClient = brokerEntityClient;
    this.defaultConfigFe = defaultConfigFe;
    this.personalisationFE2ConfigFEMapper = personalisationFE2ConfigFEMapper;
  }

  @Override
  public ConfigFE getBrokerConfig(UserInfo user, String accessToken) {
    if (user.getBrokerId() == null) {
      log.warn("BrokerId is null, returning default configuration.");
      ConfigFE defaultConfig = getFEConfiguration(null);
      defaultConfig.setCanManageUsers(user.getCanManageUsers());
      return defaultConfig;
    }

    log.info("BrokerId retrieved from UserInfo: {}", user.getBrokerId());
    Broker broker = brokerEntityClient.getBrokerById(user.getBrokerId(), accessToken);
    ConfigFE configFE = getFEConfiguration(broker);
    configFE.setCanManageUsers(user.getCanManageUsers());

    return configFE;
  }


  public ConfigFE getFEConfiguration(Broker broker) {
    if (broker != null) {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(broker.getPersonalisationFe());
    } else {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(this.defaultConfigFe);
    }
  }

}
