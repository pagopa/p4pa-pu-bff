package it.gov.pagopa.pu.bff.service.analytics;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;
  private AnalyticsService analyticsService;


  @BeforeEach
  void setUp() {
    analyticsService = new AnalyticsServiceImpl("https://test.com", authorizationServiceMock);
  }

  @Test
  void givenValidRequestWhenGenerateSupersetUrlThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "accessToken";


    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);
      String expectedResult = "https://test.com/?token=LIMITED-TOKEN";
      AccessToken expectedToken = AccessToken.builder()
        .accessToken("LIMITED-TOKEN")
        .tokenType("bearer")
        .expiresIn(3600)
        .build();

      when(authorizationServiceMock.postLimitedToken(organizationId, "superset", accessToken)).thenReturn(expectedToken);

      String result = analyticsService.generateSupersetUrl(organizationId, loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
    }
  }

}
