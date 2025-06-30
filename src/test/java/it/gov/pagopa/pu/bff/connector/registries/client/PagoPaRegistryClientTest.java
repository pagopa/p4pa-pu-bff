package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.controller.generated.PagoPaRegistryApi;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoPaRegistryClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;
  @Mock
  private PagoPaRegistryApi pagoPaRegistryApiMock;

  private PagoPaRegistryClient pagoPaRegistryClient;

  @BeforeEach
  void setUp() {
    pagoPaRegistryClient = new PagoPaRegistryClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      registriesApisHolderMock
    );
  }

  @Test
  void givenExistingPagoPaRegistryWhenGetPagoPaRegistryThenInvokeWithAccessToken() {
    String accessToken = "TOKEN";
    PagoPaRegistryDTO expectedResponse = podamFactory.manufacturePojo(PagoPaRegistryDTO.class);

    when(registriesApisHolderMock.getPagoPaRegistryApi(accessToken))
      .thenReturn(pagoPaRegistryApiMock);
    when(pagoPaRegistryApiMock.getPagoPaRegistry(expectedResponse.getRegistryId()))
      .thenReturn(expectedResponse);

    PagoPaRegistryDTO response = pagoPaRegistryClient.getPagoPaRegistry(expectedResponse.getRegistryId(),accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }

  @Test
  void givenNoPagoPaRegistryWhenGetPagoPaRegistryThenNull() {
    String accessToken = "TOKEN";
    String pagoPaRegistryId = "pagoPaRegistryId";

    when(registriesApisHolderMock.getPagoPaRegistryApi(accessToken))
      .thenReturn(pagoPaRegistryApiMock);
    when(pagoPaRegistryApiMock.getPagoPaRegistry(pagoPaRegistryId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    PagoPaRegistryDTO response = pagoPaRegistryClient.getPagoPaRegistry(pagoPaRegistryId,accessToken);

    assertNull(response);
  }
}
