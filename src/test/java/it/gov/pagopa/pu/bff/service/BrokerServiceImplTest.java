package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.bff.service.broker.BrokerServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BrokerServiceImplTest {
  @Mock
  private BrokerEntityClient brokerEntityClientMock;
  @Mock
  private DefaultConfigFe defaultConfigFeMock;
  @Mock
  private PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapperMock;
  private BrokerServiceImpl brokerService;
  private Broker entityModelBroker;
  private PersonalisationFe personalisationFe;
  private final String accessToken = "TOKEN";
  private ConfigFE defaultFEConfig;

  @BeforeEach
  void setUp() {
    entityModelBroker = new Broker();
    personalisationFe = new PersonalisationFe();
    entityModelBroker.setPersonalisationFe(personalisationFe);

    defaultFEConfig = new ConfigFE();

    brokerService = new BrokerServiceImpl(
      brokerEntityClientMock,
      defaultConfigFeMock,
      personalisationFE2ConfigFEMapperMock
    );
  }

  @Test
  void givenGetBrokerConfigWhenValidDataThenOK() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE configFE = new ConfigFE();
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(1L);
    userInfo.setCanManageUsers(true);

    Mockito.when(brokerEntityClientMock.getBrokerById(1L, accessToken)).thenReturn(entityModelBroker);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(personalisationFe)).thenReturn(configFE);

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

    Mockito.when(brokerEntityClientMock.getBrokerById(1L, accessToken)).thenReturn(null);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(defaultConfigFeMock)).thenReturn(defaultFEConfig);

    ConfigFE result = brokerService.getBrokerConfig(userInfo, accessToken);

    assertEquals(defaultFEConfig, result);
    assertEquals(userInfo.getCanManageUsers(), result.getCanManageUsers());
  }

}
