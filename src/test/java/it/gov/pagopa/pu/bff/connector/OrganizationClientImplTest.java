package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrganizationClientImplTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  @Mock
  private RestTemplate restTemplateMock;

  @Mock
  private BrokerEntityControllerApi brokerEntityControllerApiMock;
  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApiMock;

  private OrganizationClientImpl organizationClient;

  private final String accessToken = "TOKEN";

  private static final String BASE_URL = "http://test-url.com";

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    ApiClient apiClient = new ApiClient(restTemplateMock);
    apiClient.setBasePath(BASE_URL);
    brokerEntityControllerApiMock = new BrokerEntityControllerApi(apiClient);
    organizationSearchControllerApiMock = new OrganizationSearchControllerApi(apiClient);
    organizationClient = new OrganizationClientImpl(restTemplateBuilderMock, BASE_URL);
  }

  @Test
  void givenGetBrokerByIdWhenValidIdThenOK() {
    EntityModelBroker entityModelBroker = new EntityModelBroker();
    entityModelBroker.setBrokerId(0L);

    ResponseEntity<EntityModelBroker> responseEntity = new ResponseEntity<>(entityModelBroker, HttpStatus.OK);
    Mockito.when(restTemplateMock.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelBroker entityModelBroker1 = organizationClient.getBrokerById(0L, accessToken);
    assertEquals(entityModelBroker, entityModelBroker1);
  }

  @Test
  void givenGetBrokerByIdWhenInvalidIdThenKO() {
    EntityModelBroker entityModelBroker = new EntityModelBroker();
    entityModelBroker.setBrokerId(0L);

    ResponseEntity<EntityModelBroker> responseEntity = new ResponseEntity<>(entityModelBroker, HttpStatus.NOT_FOUND);
    Mockito.when(restTemplateMock.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelBroker entityModelBroker1 = organizationClient.getBrokerById(0L, accessToken);
    Assertions.assertNull(entityModelBroker1);
  }

  @Test
  void givenGetOrganizationByIpaCodeWhenValidIdThenOK() {
    EntityModelOrganization entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setIpaCode("0");

    ResponseEntity<EntityModelOrganization> responseEntity = new ResponseEntity<>(entityModelOrganization, HttpStatus.OK);
    Mockito.when(restTemplateMock.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelOrganization entityModelOrganization1 = organizationClient.getOrganizationByIpaCode("0", accessToken);
    assertEquals(entityModelOrganization, entityModelOrganization1);

  }

  @Test
  void givenGetOrganizationByIpaCodeWhenInvalidIdThenKO() {
    Mockito.when(restTemplateMock.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    EntityModelOrganization entityModelOrganization1 = organizationClient.getOrganizationByIpaCode("0", accessToken);
    Assertions.assertNull(entityModelOrganization1);
  }

}
