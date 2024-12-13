package it.gov.pagopa.pu.bff.service.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.mapper.PersonalisationFE2ConfigFEMapper;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class BrokerServiceImplTest {

  @Mock
  private OrganizationClientImpl organizationClientMock;

  @Mock
  private DefaultConfigFe defaultConfigFeMock;

  @Mock
  private PersonalisationFE2ConfigFEMapper personalisationFE2ConfigFEMapperMock;

  @InjectMocks
  private BrokerServiceImpl brokerServiceMock;

  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private EntityModelOrganization entityModelOrganization;
  private EntityModelBroker entityModelBroker;
  private PersonalisationFe personalisationFe;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");

    userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));

    entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setBrokerId(1L);

    entityModelBroker = new EntityModelBroker();
    personalisationFe = new PersonalisationFe();
    entityModelBroker.setPersonalisationFe(personalisationFe);

    // Mock SecurityUtils.getLoggedUser() to return userInfo
    Collection<? extends GrantedAuthority> authorities = null;
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  @Test
  void givenGetBrokerConfigWhenValidDataThenOK() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE configFE = new ConfigFE();
    Mockito.when(organizationClientMock.getOrganizationByIpaCode("ORG",accessToken)).thenReturn(entityModelOrganization);
    Mockito.when(organizationClientMock.getBrokerById(1L,accessToken)).thenReturn(entityModelBroker);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(personalisationFe)).thenReturn(configFE);

    ConfigFE result = brokerServiceMock.getBrokerConfig(TestUtils.getSampleUser(),accessToken);

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
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(defaultConfigFeMock)).thenReturn(configFE);

    ConfigFE result = brokerServiceMock.getBrokerConfig(new UserInfo(),accessToken);
    assertEquals(configFE, result);
  }

  @Test
  void givenGetBrokerConfigWhenNoBrokerThenOkDefault() {
    TestUtils.addSampleUserIntoSecurityContext();
    ConfigFE defaultConf = new ConfigFE();
    Mockito.when(organizationClientMock.getOrganizationByIpaCode("ORG",accessToken)).thenReturn(entityModelOrganization);
    Mockito.when(organizationClientMock.getBrokerById(1L,accessToken)).thenReturn(null);
    Mockito.when(personalisationFE2ConfigFEMapperMock.mapPersonalisationFE2ConfigFE(defaultConfigFeMock)).thenReturn(defaultConf);

    ConfigFE result = brokerServiceMock.getBrokerConfig(TestUtils.getSampleUser(),accessToken);
    assertEquals(defaultConf, result);
  }

}
