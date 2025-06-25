package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistryClient;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SilRegistryServiceImplTest {
  public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

  @Mock
  private SilRegistryClient silRegistryClientMock;

  private SilRegistryService silRegistryService;

  @BeforeEach
  void setUp() {
    silRegistryService = new SilRegistryServiceImpl(silRegistryClientMock);
  }

  @Test
  void whenGetSilRegistryThenInvokeClient() {
    String registryId = "123";
    SilRegistryDTO expectedResult = new SilRegistryDTO();

    when(silRegistryClientMock.getSilRegistry(registryId, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    SilRegistryDTO result = silRegistryService.getSilRegistry(registryId, ACCESS_TOKEN);

    assertSame(expectedResult, result);
    verify(silRegistryClientMock).getSilRegistry(registryId, ACCESS_TOKEN);
    verifyNoMoreInteractions(silRegistryClientMock);
  }
}
