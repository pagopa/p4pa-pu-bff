package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.service.broker.BrokerService;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrokerControllerTest {

  @Mock
  private BrokerService brokerService;

  @InjectMocks
  private BrokerController brokerController;

  private ConfigFE configFE;
  private UserInfo userInfo;

  @BeforeEach
  void setUp() {

    userInfo = new UserInfo();
    userInfo.setUserId("fakeUserId");
    userInfo.setMappedExternalUserId("fakeExternalId");
    userInfo.setFiscalCode("fakeFiscalCode");
    userInfo.setFamilyName("FakeFamilyName");
    userInfo.setName("FakeName");
    userInfo.setEmail("fake@example.com");
    userInfo.setIssuer("fakeIssuer");
    userInfo.setCanManageUsers(true);

    Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    configFE = new ConfigFE();
  }

  @Test
  void testGetBrokerConfig() {
    when(brokerService.getBrokerConfig(userInfo, "fakeAccessToken")).thenReturn(configFE);

    ResponseEntity<ConfigFE> response = brokerController.getBrokerConfig();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(configFE, response.getBody());

    verify(brokerService, times(1)).getBrokerConfig(userInfo, "fakeAccessToken");
  }

}

