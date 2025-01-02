package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.bff.service.broker.BrokerServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserOrganizationRoles;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BrokerServiceImplTest {

  @Mock
  private OrganizationClientImpl organizationClientMock;

  @Mock
  private DefaultConfigFe defaultConfigFeMock;

  @Mock
  private PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapperMock;
  private BrokerServiceImpl brokerService;

  private EntityModelOrganization entityModelOrganization;
  private EntityModelBroker entityModelBroker;
  private PersonalisationFe personalisationFe;

  private final String accessToken = "TOKEN";

  private ConfigFE defaultFEConfig;

  @BeforeEach
  void setUp() {
    UserOrganizationRoles userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");

    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));

    entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setBrokerId(1L);

    entityModelBroker = new EntityModelBroker();
    personalisationFe = new PersonalisationFe();
    entityModelBroker.setPersonalisationFe(personalisationFe);

    defaultFEConfig = new ConfigFE();

    // Mock SecurityUtils.getLoggedUser() to return userInfo
    Collection<? extends GrantedAuthority> authorities = null;
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    brokerService = new BrokerServiceImpl(organizationClientMock,defaultConfigFeMock,personalisationFE2ConfigFEMapperMock);
  }

  @Test
  void givenGetBrokerConfigWhenValidDataThenOK() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE configFE = new ConfigFE();
    Mockito.when(organizationClientMock.getOrganizationByIpaCode("ORG",accessToken)).thenReturn(entityModelOrganization);
    Mockito.when(organizationClientMock.getBrokerById(1L,accessToken)).thenReturn(entityModelBroker);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(personalisationFe)).thenReturn(configFE);

    ConfigFE result = brokerService.getBrokerConfig(TestUtils.getSampleUser(),accessToken);

    assertEquals(personalisationFe.getFooterAccessibilityUrl(), result.getFooterAccessibilityUrl());
    assertEquals(personalisationFe.getFooterGDPRUrl(),result.getFooterGDPRUrl());
    assertEquals(personalisationFe.getFooterDescText(),result.getFooterDescText());
    assertEquals(personalisationFe.getFooterTermsCondUrl(),result.getFooterTermsCondUrl());
    assertEquals(personalisationFe.getHeaderAssistanceUrl(),result.getHeaderAssistanceUrl());
    assertEquals(personalisationFe.getLogoFooterImg(),result.getLogoFooterImg());
  }

  @Test
  void givenGetBrokerConfigWhenNoOrganizationThenOkDefault() {
    ConfigFE configFE = new ConfigFE();
    ReflectionTestUtils.setField(brokerService, "defaultFEConfig", configFE);

    ConfigFE result = brokerService.getBrokerConfig(TestUtils.getSampleUser(false),accessToken);
    assertEquals(defaultFEConfig, result);
  }

  @Test
  void givenGetBrokerConfigWhenNoBrokerThenOkDefault() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE defaultConf = new ConfigFE();
    Mockito.when(organizationClientMock.getOrganizationByIpaCode("ORG",accessToken)).thenReturn(entityModelOrganization);
    Mockito.when(organizationClientMock.getBrokerById(1L,accessToken)).thenReturn(null);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(defaultConfigFeMock)).thenReturn(defaultConf);

    ConfigFE result = brokerService.getBrokerConfig(TestUtils.getSampleUser(),accessToken);
    assertEquals(defaultConf, result);
  }

}
