package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ExtendWith(MockitoExtension.class)
class SilRegistryRetrieverServiceImplTest {
  @Mock
  private AuthorizationService authorizationServiceMock;

  @Mock
  private SilRegistryService silRegistryServiceMock;

  private SilRegistryRetrieverService silRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    silRegistryRetrieverService = new SilRegistryRetrieverServiceImpl(authorizationServiceMock, silRegistryServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, silRegistryServiceMock);
  }

  @Test
  void givenValidUserWhenGetSilRegistryThenOk() {
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    SilRegistryDTO expectedDTO = new SilRegistryDTO();
    expectedDTO.setRegistryId(registryId);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.when(silRegistryServiceMock.getSilRegistry(registryId, accessToken))
      .thenReturn(expectedDTO);

    SilRegistryDTO result = silRegistryRetrieverService.getSilRegistry(registryId, loggedUser, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedDTO, result);

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(silRegistryServiceMock).getSilRegistry(registryId, accessToken);
    Mockito.verifyNoMoreInteractions(silRegistryServiceMock);
  }

  @Test
  void givenInvalidUserWhenGetSilRegistryThenAuthorizationDeniedException() {
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-unauthorized");

    Mockito.doThrow(new AuthorizationDeniedException("Access denied"))
      .when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      silRegistryRetrieverService.getSilRegistry(registryId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verifyNoInteractions(silRegistryServiceMock);
  }
}
