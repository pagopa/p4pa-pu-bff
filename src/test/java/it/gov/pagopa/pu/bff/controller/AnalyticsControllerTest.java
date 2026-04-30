package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.SupersetUrlResponseDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.analytics.AnalyticsService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

  @Mock
  private AnalyticsService analyticsServiceMock;
  @InjectMocks
  private AnalyticsController analyticsController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @Test
  void givenAuthorizedRequestWhenGenerateSupersetUrlThenOK() {
    // Given
    Long organizationId = 1L;
    String url = "https://test.com";
    SupersetUrlResponseDTO expectedResponse = new SupersetUrlResponseDTO();
    expectedResponse.setAuthorizedUrl(url);

    when(analyticsServiceMock.generateSupersetUrl(organizationId, loggedUser, accessToken))
      .thenReturn(url);
    // When
    ResponseEntity<SupersetUrlResponseDTO> result = analyticsController.generateSupersetUrl(organizationId);
    // Then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(expectedResponse, result.getBody());
  }
}
