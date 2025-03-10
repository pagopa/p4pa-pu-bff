package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

  @Mock
  private TransferClient client;

  private TransferService service;

  @BeforeEach
  void setUp() {
    service = new TransferServiceImpl(client);
  }

  @Test
  void whenGetTransfersThenInvokeClient() {
    Long installmentId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    CollectionModelTransfer expectedResult = new CollectionModelTransfer();

    when(client.getTransfers(Mockito.same(installmentId), Mockito.same(operatorExternalUserId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelTransfer result = service.getTransfers(installmentId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }
}
