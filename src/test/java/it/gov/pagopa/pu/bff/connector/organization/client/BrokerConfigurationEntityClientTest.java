package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.BrokerConfigurationEntityControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerConfigurationEntityClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private BrokerConfigurationEntityControllerApi brokerConfigurationEntityControllerApiMock;

  private BrokerConfigurationEntityClient client;

  @BeforeEach
  void setUp() {
    client = new BrokerConfigurationEntityClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      brokerConfigurationEntityControllerApiMock
    );
  }

  @Test
  void whenGetBrokerConfigurationThenInvokeWithAccessToken() {
    Long brokerId = 1L;
    String accessToken = "ACCESSTOKEN";
    BrokerConfiguration expectedResult = new BrokerConfiguration();

    when(organizationApisHolderMock.getBrokerConfigurationEntityControllerApi(accessToken))
      .thenReturn(brokerConfigurationEntityControllerApiMock);
    when(brokerConfigurationEntityControllerApiMock.crudGetBrokerconfiguration(String.valueOf(brokerId)))
      .thenReturn(expectedResult);

    BrokerConfiguration result = client.getBrokerConfiguration(brokerId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNotFoundWhenGetBrokerConfigurationThenNull() {
    Long brokerId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolderMock.getBrokerConfigurationEntityControllerApi(accessToken))
      .thenReturn(brokerConfigurationEntityControllerApiMock);
    when(brokerConfigurationEntityControllerApiMock.crudGetBrokerconfiguration(String.valueOf(brokerId)))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    BrokerConfiguration result = client.getBrokerConfiguration(brokerId, accessToken);

    assertNull(result);
  }
}
