package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.BrokerConfigurationEntityClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrokerConfigurationServiceImplTest {
  @Mock
  private BrokerConfigurationEntityClient clientMock;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  BrokerConfigurationService service;

  @BeforeEach
  void setUp() {
    service = new BrokerConfigurationServiceImpl(clientMock);
  }

  @AfterEach
  void mockitoVerify(){
    Mockito.verifyNoMoreInteractions(clientMock);
  }

  @Test
  void whenGetBrokerConfigurationThenInvokeClient() {
    String accessToken = "ACCESS_TOKEN";
    Long brokerId = 1L;
    BrokerConfiguration expectedResult = podamFactory.manufacturePojo(BrokerConfiguration.class);

    Mockito.when(clientMock.getBrokerConfiguration(brokerId, accessToken)).thenReturn(expectedResult);

    BrokerConfiguration result = service.getBrokerConfiguration(brokerId, accessToken);
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }
}
