package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.TransferApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;

  @Mock
  private TransferApi transferApiMock;

  private TransferClient transferClient;

  @BeforeEach
  void setUp() {
    transferClient = new TransferClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock,
      transferApiMock);
  }

  @Test
  void whenGetTransfersThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String category = "001122233";

    when(debtPositionApisHolderMock.getTransferApi(accessToken))
      .thenReturn(transferApiMock);

    doNothing().when(transferApiMock).validateTaxonomyCategory(category);

    assertDoesNotThrow(
      () -> transferClient.validateTaxonomyCategory(category, accessToken));
  }
}
