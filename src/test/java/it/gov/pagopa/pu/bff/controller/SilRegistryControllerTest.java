package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SilRegistryControllerTest {

  @Mock
  private SilRegistryRetrieverService silRegistryRetrieverServiceMock;

  @InjectMocks
  private SilRegistryController silRegistryController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(silRegistryRetrieverServiceMock);
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetSilRegistryThenOk() {
    long organizationId = 1L;
    String registryId = "200";

    SilRegistryDTO expectedDTO = new SilRegistryDTO();
    expectedDTO.setRegistryId(registryId);

    Mockito.when(silRegistryRetrieverServiceMock.getSilRegistry(
        Mockito.eq(organizationId),
        Mockito.eq(registryId),
        Mockito.same(loggedUser),
        Mockito.same(accessToken)))
      .thenReturn(expectedDTO);

    ResponseEntity<SilRegistryDTO> response = silRegistryController.getSilRegistry(organizationId, registryId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedDTO, response.getBody());
  }

  @Test
  void givenIncorrectRequestWhenGetSilRegistryThenNotFound() {
    long organizationId = 1L;
    String registryId = "999";

    Mockito.when(silRegistryRetrieverServiceMock.getSilRegistry(
        organizationId, registryId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<SilRegistryDTO> response = silRegistryController.getSilRegistry(organizationId, registryId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}

