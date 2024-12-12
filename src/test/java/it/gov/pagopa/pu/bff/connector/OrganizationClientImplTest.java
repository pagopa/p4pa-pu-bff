package it.gov.pagopa.pu.bff.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
class OrganizationClientImplTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  RestTemplate restTemplate;

  @Mock
  private BrokerEntityControllerApi brokerEntityControllerApi;
  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApi;

  @InjectMocks
  private OrganizationClientImpl organizationClient;

  private static final String BASE_URL = "http://test-url.com";
  @BeforeEach
  void setUp() {
    restTemplate = Mockito.mock(RestTemplate.class);
    Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
    Mockito.when(restTemplate.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(BASE_URL);
    brokerEntityControllerApi = new BrokerEntityControllerApi(apiClient);
    organizationSearchControllerApi = new OrganizationSearchControllerApi(apiClient);
    organizationClient = new OrganizationClientImpl(restTemplateBuilder,BASE_URL);
  }
  @Test
  void givenGetBrokerByIdWhenValidIdThenOK(){
    EntityModelBroker entityModelBroker = new EntityModelBroker();
    entityModelBroker.setBrokerId(0L);

    ResponseEntity<EntityModelBroker> responseEntity = new ResponseEntity<>(entityModelBroker,HttpStatus.OK);
    Mockito.when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelBroker entityModelBroker1 = organizationClient.getBrokerById("0");
    assertEquals(entityModelBroker, entityModelBroker1);
  }

  @Test
  void givenGetBrokerByIdWhenInvalidIdThenKO(){
    EntityModelBroker entityModelBroker = new EntityModelBroker();
    entityModelBroker.setBrokerId(0L);

    ResponseEntity<EntityModelBroker> responseEntity = new ResponseEntity<>(entityModelBroker,HttpStatus.NOT_FOUND);
    Mockito.when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelBroker entityModelBroker1 = organizationClient.getBrokerById("0");
    Assertions.assertNull(entityModelBroker1);
  }
  @Test
  void givenGetOrganizationByIpaCodeWhenValidIdThenOK(){
    EntityModelOrganization entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setIpaCode("0");

    ResponseEntity<EntityModelOrganization> responseEntity = new ResponseEntity<>(entityModelOrganization,HttpStatus.OK);
    Mockito.when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelOrganization entityModelOrganization1 = organizationClient.getOrganizationByIpaCode("0");
    assertEquals(entityModelOrganization, entityModelOrganization1);

  }

  @Test
  void givenGetOrganizationByIpaCodeWhenInvalidIdThenKO(){
    EntityModelOrganization entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setIpaCode("0");

    ResponseEntity<EntityModelOrganization> responseEntity = new ResponseEntity<>(entityModelOrganization,HttpStatus.NOT_FOUND);
    Mockito.when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

    EntityModelOrganization entityModelOrganization1 = organizationClient.getOrganizationByIpaCode("0");
    Assertions.assertNull(entityModelOrganization1);
  }

}
