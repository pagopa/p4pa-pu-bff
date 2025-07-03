package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.PagoPaRegistryClient;
import it.gov.pagopa.pu.bff.connector.registries.client.PagoPaRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoPaRegistryServiceTest {
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PagoPaRegistrySearchClient pagoPaRegistrySearchClientMock;
  @Mock
  private PagoPaRegistryClient pagoPaRegistryClientMock;
  private PagoPaRegistryService service;

  @BeforeEach
  void setUp() {
    service = new PagoPaRegistryServiceImpl(pagoPaRegistrySearchClientMock,pagoPaRegistryClientMock);
  }

  @Test
  void whenSearchByFiltersThenInvokeClient() {
    String orgFiscalCode = "orgFiscalCode";
    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(10);
    String accessToken = "ACCESSTOKEN";
    PagedModelPagoPaRegistry expectedResult = new PagedModelPagoPaRegistry();

    when(pagoPaRegistrySearchClientMock.searchByFilters(orgFiscalCode,filters,pageable,accessToken))
            .thenReturn(expectedResult);

    PagedModelPagoPaRegistry result = service.searchByFilters(orgFiscalCode,filters,pageable,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetPagoPaRegistryThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    PagoPaRegistryDTO expectedResult = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);

    when(pagoPaRegistryClientMock.getPagoPaRegistry(expectedResult.getRegistryId(),accessToken))
            .thenReturn(expectedResult);

    PagoPaRegistryDTO result = service.getPagoPaRegistry(expectedResult.getRegistryId(),accessToken);

    assertSame(expectedResult, result);
  }
}
