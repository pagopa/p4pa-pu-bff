package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.BrokerConfigurationService;
import it.gov.pagopa.pu.bff.connector.organization.BrokerService;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class BrokerRetrieverServiceImpl implements BrokerRetrieverService {

  private final BrokerService brokerService;
  private final PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper;
  private final DefaultConfigFe defaultConfigFe;
  private final ConfigFE defaultFEConfig;
  private final BrokerConfigurationService brokerConfigurationService;

  public BrokerRetrieverServiceImpl(BrokerService brokerService,
                                    DefaultConfigFe defaultConfigFe,
                                    PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapper, BrokerConfigurationService brokerConfigurationService) {
    this.brokerService = brokerService;
    this.defaultConfigFe = defaultConfigFe;
    this.personalisationFE2ConfigFEMapper = personalisationFE2ConfigFEMapper;
    this.brokerConfigurationService = brokerConfigurationService;
    this.defaultFEConfig = getFEConfiguration(null, null, null);
  }

  @Override
  public ConfigFE getBrokerConfig(UserInfo user, String accessToken) {
    if (user.getBrokerId() == null) {
      log.warn("BrokerId is null, returning default configuration.");
      return this.defaultFEConfig;
    }

    log.info("BrokerId retrieved from UserInfo: {}", user.getBrokerId());
    Broker broker = brokerService.getBrokerById(user.getBrokerId(), accessToken);

    return getFEConfiguration(broker, user, accessToken);
  }


  public ConfigFE getFEConfiguration(Broker broker, UserInfo userInfo, String accessToken) {
    if (broker != null) {
      BrokerConfiguration brokerConfiguration = brokerConfigurationService.getBrokerConfiguration(broker.getBrokerId(), accessToken);
      if(Objects.isNull(brokerConfiguration)) {
        throw new NotFoundException("BROKER_CONFIGURATION_NOT_FOUND","Broker configuration having broker id "+broker.getBrokerId()+" not found");
      }
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(brokerConfiguration.getPersonalisationFe(), broker, userInfo);
    } else {
      return personalisationFE2ConfigFEMapper.mapPersonalisationFE2ConfigFE(this.defaultConfigFe, null, userInfo);
    }
  }

}
