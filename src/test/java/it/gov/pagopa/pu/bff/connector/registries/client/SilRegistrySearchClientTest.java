package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.controller.generated.SilRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
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
class SilRegistrySearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;

  @Mock
  private SilRegistrySearchControllerApi silRegistrySearchControllerApiMock;

  private SilRegistrySearchClient silRegistrySearchClient;

  @BeforeEach
  void setUp() {
    silRegistrySearchClient = new SilRegistrySearchClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      registriesApisHolderMock,
      silRegistrySearchControllerApiMock
    );
  }

  @Test
  void whenSearchByFiltersThenInvokeWithAccessToken() {
    String accessToken = "TOKEN";
    String orgFiscalCode = "orgFiscalCode";
    SilRegistryFiltersDTO filters = podamFactory.manufacturePojo(SilRegistryFiltersDTO.class);
    PagedModelSilRegistry expectedResponse = podamFactory.manufacturePojo(PagedModelSilRegistry.class);

    when(registriesApisHolderMock.getSilRegistrySearchControllerApi(accessToken))
      .thenReturn(silRegistrySearchControllerApiMock);
    when(silRegistrySearchControllerApiMock.crudSilRegistriesSearchByFilters(
      filters.getEventType(),
      filters.getEventDate().getFrom(),
      filters.getEventDate().getTo(),
      orgFiscalCode,
      filters.getIuv(),
      filters.getOutcome(),
      0,
      10,
      Collections.emptyList()
    ))
      .thenReturn(expectedResponse);

    PagedModelSilRegistry response = silRegistrySearchClient.searchByFilters(orgFiscalCode, filters, Pageable.ofSize(10), accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }
}
