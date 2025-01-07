package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
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

@ExtendWith(MockitoExtension.class)
class BrokerEntityClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private BrokerEntityControllerApi brokerEntityControllerApiMock;

  private BrokerEntityClient brokerEntityClient;

  @BeforeEach
  void setUp() {
    brokerEntityClient = new BrokerEntityClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder
    );
  }

  @Test
  void whenGetOrganizationByIpaCodeThenInvokeWithAccessToken() {
    // Given
    Long brokerId = 0L;
    String accessToken = "ACCESSTOKEN";
    EntityModelBroker expectedResult = new EntityModelBroker();

    Mockito.when(organizationApisHolder.getBrokerEntityControllerApi(accessToken))
      .thenReturn(brokerEntityControllerApiMock);
    Mockito.when(brokerEntityControllerApiMock.getItemResourceBrokerGet(brokerId.toString()))
      .thenReturn(expectedResult);

    // When
    EntityModelBroker result = brokerEntityClient.getBrokerById(brokerId, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIpaCodeWhenGetOrganizationByIpaCodeThenNull() {
    // Given
    Long brokerId = 0L;
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolder.getBrokerEntityControllerApi(accessToken))
      .thenReturn(brokerEntityControllerApiMock);
    Mockito.when(brokerEntityControllerApiMock.getItemResourceBrokerGet(brokerId.toString()))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    // When
    EntityModelBroker result = brokerEntityClient.getBrokerById(brokerId, accessToken);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetOrganizationByIpaCodeThenThrowIt() {
    // Given
    Long brokerId = 0L;
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getBrokerEntityControllerApi(accessToken))
      .thenReturn(brokerEntityControllerApiMock);
    Mockito.when(brokerEntityControllerApiMock.getItemResourceBrokerGet(brokerId.toString()))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> brokerEntityClient.getBrokerById(brokerId, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenExceptionWhenGetOrganizationByIpaCodeThenThrowIt() {
    // Given
    Long brokerId = 0L;
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getBrokerEntityControllerApi(accessToken))
      .thenReturn(brokerEntityControllerApiMock);
    Mockito.when(brokerEntityControllerApiMock.getItemResourceBrokerGet(brokerId.toString()))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> brokerEntityClient.getBrokerById(brokerId, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }
}
