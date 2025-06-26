package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.controller.generated.PagoPaRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoPaRegistrySearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;
  @Mock
  private PagoPaRegistrySearchControllerApi pagoPaRegistrySearchControllerApiMock;

  private PagoPaRegistrySearchClient pagoPaRegistrySearchClient;

  @BeforeEach
  void setUp() {
    pagoPaRegistrySearchClient = new PagoPaRegistrySearchClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      registriesApisHolderMock,
      pagoPaRegistrySearchControllerApiMock
    );
  }

  @Test
  void whenSearchByFiltersThenInvokeWithAccessToken() {
    String accessToken = "TOKEN";
    String orgFiscalCode = "orgFiscalCode";
    PagoPaRegistryFiltersDTO filters = podamFactory.manufacturePojo(PagoPaRegistryFiltersDTO.class);
    PagedModelPagoPaRegistry expectedResponse = podamFactory.manufacturePojo(PagedModelPagoPaRegistry.class);

    when(registriesApisHolderMock.getPagoPaRegistrySearchControllerApi(accessToken))
      .thenReturn(pagoPaRegistrySearchControllerApiMock);
    when(pagoPaRegistrySearchControllerApiMock.crudPagopaRegistriesSearchByFilters(
            filters.getEventType(),
            filters.getEventDate().getFrom(),
            filters.getEventDate().getTo(),
            orgFiscalCode,
            filters.getIuv(),
            0,
            10,
            Collections.emptyList()
    ))
      .thenReturn(expectedResponse);

    PagedModelPagoPaRegistry response = pagoPaRegistrySearchClient.searchByFilters(orgFiscalCode,filters, Pageable.ofSize(10),accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }
}
