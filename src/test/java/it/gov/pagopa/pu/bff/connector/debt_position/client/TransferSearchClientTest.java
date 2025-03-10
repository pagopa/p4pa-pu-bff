package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.TransferSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferSearchClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;

  @Mock
  private TransferSearchControllerApi transferSearchControllerApiMock;

  private TransferSearchClient transferSearchClient;

  @BeforeEach
  void setUp() {
    transferSearchClient = new TransferSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, transferSearchControllerApiMock);
  }

  @Test
  void whenGetTransfersThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    CollectionModelTransfer expectedResult = new CollectionModelTransfer();

    long installmentId = 1L;
    String operatorExternalUserId = "operator123";

    when(debtPositionApisHolderMock.getTransferSearchControllerApi(accessToken))
      .thenReturn(transferSearchControllerApiMock);

    when(transferSearchControllerApiMock.crudTransfersFindAuthorizedByInstallmentId(
      String.valueOf(installmentId), operatorExternalUserId))
      .thenReturn(expectedResult);

    CollectionModelTransfer result = transferSearchClient.getTransfers(installmentId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }
}
