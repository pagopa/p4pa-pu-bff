package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.BrokerConfigurationService;
import it.gov.pagopa.pu.bff.connector.organization.BrokerService;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.bff.service.broker.BrokerRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;
import it.gov.pagopa.pu.organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class BrokerRetrieverServiceImplTest {
  @Mock
  private BrokerService brokerServiceMock;
  @Mock
  private DefaultConfigFe defaultConfigFeMock;
  @Mock
  private PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapperMock;
  @Mock
  private BrokerConfigurationService brokerConfigurationServiceMock;

  private BrokerRetrieverServiceImpl brokerService;
  private Broker entityModelBroker;
  private PersonalisationFe personalisationFe;
  private final String accessToken = "TOKEN";
  private ConfigFE defaultFEConfig;
  private final BrokerConfiguration brokerConfiguration = new BrokerConfiguration();

  @BeforeEach
  void setUp() {
    entityModelBroker = new Broker();
    entityModelBroker.setBrokerId(1L);
    personalisationFe = new PersonalisationFe();
    brokerConfiguration.setPersonalisationFe(personalisationFe);

    defaultFEConfig = new ConfigFE();

    brokerService = new BrokerRetrieverServiceImpl(
      brokerServiceMock,
      defaultConfigFeMock,
      personalisationFE2ConfigFEMapperMock,
      brokerConfigurationServiceMock
    );
  }

  @Test
  void givenGetBrokerConfigWhenValidDataThenOK() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE configFE = new ConfigFE();
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(1L);
    userInfo.setCanManageUsers(true);

    Mockito.when(brokerServiceMock.getBrokerById(1L, accessToken)).thenReturn(entityModelBroker);
    Mockito.when(brokerConfigurationServiceMock.getBrokerConfiguration(1L, accessToken))
      .thenReturn(brokerConfiguration);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(personalisationFe, entityModelBroker, userInfo)).thenReturn(configFE);

    ConfigFE result = brokerService.getBrokerConfig(userInfo, accessToken);

    assertEquals(personalisationFe.getFooterAccessibilityUrl(), result.getFooterAccessibilityUrl());
    assertEquals(personalisationFe.getFooterGDPRUrl(), result.getFooterGDPRUrl());
    assertEquals(personalisationFe.getFooterDescText(), result.getFooterDescText());
    assertEquals(personalisationFe.getFooterTermsCondUrl(), result.getFooterTermsCondUrl());
    assertEquals(personalisationFe.getHeaderAssistanceUrl(), result.getHeaderAssistanceUrl());
    assertEquals(personalisationFe.getLogoFooterImg(), result.getLogoFooterImg());
  }


  @Test
  void givenGetBrokerConfigWhenBrokerNotFoundThenDefaultConfig() {
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(1L);
    userInfo.setCanManageUsers(false);

    Mockito.when(brokerServiceMock.getBrokerById(1L, accessToken)).thenReturn(null);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(defaultConfigFeMock, null, userInfo)).thenReturn(defaultFEConfig);

    ConfigFE result = brokerService.getBrokerConfig(userInfo, accessToken);

    assertSame(defaultFEConfig, result);
  }

  @Test
  void givenNotFoundConfigurationWhenGetBrokerConfigThenOK() {
    TestUtils.addSampleUserIntoSecurityContext();
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(1L);
    userInfo.setCanManageUsers(true);

    Mockito.when(brokerServiceMock.getBrokerById(1L, accessToken)).thenReturn(entityModelBroker);
    Mockito.when(brokerConfigurationServiceMock.getBrokerConfiguration(1L, accessToken))
      .thenReturn(null);

    ResourceNotFoundException result = Assertions.assertThrows(
      ResourceNotFoundException.class,
      ()-> brokerService.getBrokerConfig(userInfo, accessToken));

    Assertions.assertEquals("BROKER_CONFIGURATION_NOT_FOUND", result.getCode());
  }


}
