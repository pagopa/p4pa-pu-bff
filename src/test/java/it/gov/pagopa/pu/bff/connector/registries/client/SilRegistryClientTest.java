package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.registries.controller.generated.SilRegistryApi;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SilRegistryClientTest {
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;

  @Mock
  private SilRegistryApi silRegistryApiMock;

  private SilRegistryClient silRegistryClient;

  @BeforeEach
  void setUp() {
    silRegistryClient = new SilRegistryClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      registriesApisHolderMock
    );
  }

  @Test
  void whenGetSilRegistryThenReturnSilRegistryDTO() {
    String registryId = "123";
    String accessToken = "ACCESSTOKEN";
    SilRegistryDTO expectedResult = new SilRegistryDTO();
    expectedResult.setRegistryId(registryId);

    when(registriesApisHolderMock.getSilRegistryApi(accessToken))
      .thenReturn(silRegistryApiMock);
    when(silRegistryApiMock.getSilRegistry(registryId))
      .thenReturn(expectedResult);

    SilRegistryDTO result = silRegistryClient.getSilRegistry(registryId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }
}
