package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthorizationService authorizationService;

  @InjectMocks
  private AuthenticationController authenticationController;

  private AccessTokenDTO accessTokenDTO;

  @BeforeEach
  void setUp() {
    accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setAccessToken("fake-access-token");
    accessTokenDTO.setExpiresIn(3600);
    accessTokenDTO.setTokenType("bearer");
  }

  @Test
  void testPostToken() {
    String idToken = "validIdToken";

    when(authorizationService.postToken(idToken)).thenReturn(accessTokenDTO);

    ResponseEntity<AccessTokenDTO> response = authenticationController.postToken(idToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("fake-access-token", response.getBody().getAccessToken());
    assertEquals(3600, response.getBody().getExpiresIn());
    assertEquals("bearer", response.getBody().getTokenType());

    verify(authorizationService, times(1)).postToken(idToken);
  }

}
