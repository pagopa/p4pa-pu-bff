package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistryClient;
import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SilRegistryServiceImplTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

  @Mock
  private SilRegistryClient silRegistryClientMock;

  @Mock
  private SilRegistrySearchClient silRegistrySearchClientMock;

  private SilRegistryService silRegistryService;

  @BeforeEach
  void setUp() {
    silRegistryService = new SilRegistryServiceImpl(silRegistryClientMock, silRegistrySearchClientMock);
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

  @Test
  void whenSearchByFiltersThenInvokeClient() {
    String orgFiscalCode = "orgFiscalCode";
    SilRegistryFiltersDTO filters = podamFactory.manufacturePojo(SilRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    PagedModelSilRegistry expectedResult = new PagedModelSilRegistry();

    when(silRegistrySearchClientMock.searchByFilters(orgFiscalCode, filters, pageable, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    PagedModelSilRegistry result = silRegistryService.searchByFilters(orgFiscalCode, filters, pageable, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
