package it.gov.pagopa.pu.bff.service.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.config.DefaultConfigFe;
import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.model.generated.PersonalisationFe;
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
  private OrganizationClientImpl organizationClient;

  @Mock
  private DefaultConfigFe defaultConfigFe;

  @InjectMocks
  private BrokerServiceImpl brokerService;

  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private EntityModelOrganization entityModelOrganization;
  private EntityModelBroker entityModelBroker;
  private PersonalisationFe personalisationFe;

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
    Mockito.when(organizationClient.getOrganizationByIpaCode("testIpaCode")).thenReturn(entityModelOrganization);
    Mockito.when(organizationClient.getBrokerById("1")).thenReturn(entityModelBroker);

    PersonalisationFe result = brokerService.getBrokerConfig();
    assertEquals(personalisationFe, result);
  }

  @Test
  void givenGetBrokerConfigWhenNoOrganizationThenOkDefault() {
    PersonalisationFe result = brokerService.getBrokerConfig();
    assertEquals(defaultConfigFe, result);
  }

  @Test
  void givenGetBrokerConfigWhenNoBrokerThenOkDefault() {
    Mockito.when(organizationClient.getOrganizationByIpaCode("testIpaCode")).thenReturn(entityModelOrganization);
    Mockito.when(organizationClient.getBrokerById("1")).thenReturn(null);

    PersonalisationFe result = brokerService.getBrokerConfig();
    assertEquals(defaultConfigFe, result);
  }

}
