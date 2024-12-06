package it.gov.pagopa.pu.bff.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import it.gov.pagopa.pu.bff.dto.UserInfoDTO;
import it.gov.pagopa.pu.bff.dto.UserOrganizationRolesDTO;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootTest(
  classes = {AuthorizationService.class, AuthorizationServiceTest.AuthorizationServiceTestConfiguration.class},
  webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableWireMock({
  @ConfigureWireMock(name = "auth-server", properties = "app.auth.base-url")
})
@EnableConfigurationProperties
class AuthorizationServiceTest {

  @Autowired
  private AuthorizationService authorizationService;

  @InjectWireMock(value = "auth-server")
  private WireMockServer wireMockServer;

  @TestConfiguration
  public static class AuthorizationServiceTestConfiguration {
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
      return new RestTemplateBuilder();
    }
  }

  @Test
  void givenValidAccessTokenWhenValidateTokenThenOk() {
    // When
    UserInfoDTO result = authorizationService.validateToken("ACCESSTOKEN");

    // Then
    Assertions.assertEquals(
      UserInfoDTO.builder()
        .userId("e1d9c534-86a9-4039-80da-8aa7a33ac9e7")
        .fiscalCode("DMEMPY15L21L736U")
        .familyName("demo")
        .name("demo")
        .issuer("https://dev.selfcare.pagopa.it")
        .organizationAccess("SELC_99999999990")
        .organizations(List.of(UserOrganizationRolesDTO.builder()
          .operatorId("133e9c1b-dfc5-43ea-98a7-f64f30613074")
          .organizationIpaCode("SELC_99999999990")
          .roles(List.of("ROLE_ADMIN"))
          .build()))
        .build(),
      result
    );
  }

  @Test
  void givenInvalidAccessTokenWhenValidateTokenThenInvalidAccessTokenException() {
    // When
    InvalidAccessTokenException result = Assertions.assertThrows(InvalidAccessTokenException.class,
      () -> authorizationService.validateToken("INVALIDACCESSTOKEN"));

    // Then
    Assertions.assertEquals(
      "Bad Access Token provided",
      result.getMessage()
    );
  }

  @Test
  void givenUnexpectedErrorWhenValidateTokenThenIDInvalidAccessTokenException() {
    // When
    InvalidAccessTokenException result = Assertions.assertThrows(InvalidAccessTokenException.class,
      () -> authorizationService.validateToken("EXCEPTION"));

    // Then
    Assertions.assertEquals(
      "Something gone wrong while validate token",
      result.getMessage()
    );
  }


}
