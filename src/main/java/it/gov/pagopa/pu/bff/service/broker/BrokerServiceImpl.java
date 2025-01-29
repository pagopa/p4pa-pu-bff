package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrokerServiceImpl implements BrokerService {

  private final BrokerEntityClient brokerEntityClient;
  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;
  private final DefaultConfigFe defaultConfigFe;
  private final ConfigFE defaultFEConfig;

  public BrokerServiceImpl(BrokerEntityClient brokerEntityClient,
                           DefaultConfigFe defaultConfigFe,
                           PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper) {
    this.brokerEntityClient = brokerEntityClient;
    this.defaultConfigFe = defaultConfigFe;
    this.personalisationFE2ConfigFEMapper = personalisationFE2ConfigFEMapper;
    this.defaultFEConfig = getFEConfiguration(null, null);
  }

  @Override
  public ConfigFE getBrokerConfig(UserInfo user, String accessToken) {
    if (user.getBrokerId() == null) {
      log.warn("BrokerId is null, returning default configuration.");
      return this.defaultFEConfig;
    }

    log.info("BrokerId retrieved from UserInfo: {}", user.getBrokerId());
    Broker broker = brokerEntityClient.getBrokerById(user.getBrokerId(), accessToken);

    return getFEConfiguration(broker, user);
  }


  public ConfigFE getFEConfiguration(Broker broker, UserInfo userInfo) {
    if (broker != null) {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(broker.getPersonalisationFe(), broker, userInfo);
    } else {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(this.defaultConfigFe, null, userInfo);
    }
  }

}
