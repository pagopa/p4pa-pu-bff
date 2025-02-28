package it.gov.pagopa.pu.bff.connector.organization;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrokerServiceTest {

  @Mock
  private BrokerEntityClient client;

  private BrokerService service;

  @BeforeEach
  void setUp() {
    service = new BrokerServiceImpl(client);
  }

  @Test
  void testGetBrokerById() {
    Long brokerId = 1L;
    String accessToken = "accessToken";

    Broker expected = new Broker();
    when(client.getBrokerById(Mockito.same(brokerId), Mockito.same(accessToken)))
      .thenReturn(expected);

    Broker result = service.getBrokerById(brokerId, accessToken);

    assertSame(expected, result);
  }
}
