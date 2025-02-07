package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrokerClientServiceTest {

  @Mock
  private BrokerEntityClient client;

  private BrokerClientService service;

  @BeforeEach
  void setUp() {
    service = new BrokerClientServiceImpl(client);
  }

  @Test
  void testGetBrokerById() {
    Long brokerId = 1L;
    String accessToken = "accessToken";

    Broker expected = new Broker();
    when(client.getBrokerById(brokerId, accessToken)).thenReturn(expected);

    Broker result = service.getBrokerById(brokerId, accessToken);

    assertEquals(expected, result);
  }
}
