package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

  @Mock
  private TransferClient clientMock;
  @Mock
  private TransferSearchClient searchClientMock;

  private TransferService service;

  @BeforeEach
  void setUp() {
    service = new TransferServiceImpl(clientMock, searchClientMock);
  }

  @Test
  void whenGetTransfersThenInvokeClient() {
    Long installmentId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    CollectionModelTransfer expectedResult = new CollectionModelTransfer();

    when(searchClientMock.getTransfers(Mockito.same(installmentId), Mockito.same(operatorExternalUserId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelTransfer result = service.getTransfers(installmentId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenValidateTaxonomyCategoryThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    String category = "001122233";

    doNothing().when(clientMock).validateTaxonomyCategory(Mockito.same(category), Mockito.same(accessToken));

    assertDoesNotThrow(() -> service.validateTaxonomyCategory(category, accessToken));
  }
}
