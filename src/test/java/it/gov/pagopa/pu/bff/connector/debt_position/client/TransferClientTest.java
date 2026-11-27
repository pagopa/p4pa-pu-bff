package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.client.generated.TransferApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

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
    String orgFiscalCode = "orgFiscalCode";

    when(debtPositionApisHolderMock.getTransferApi(accessToken))
      .thenReturn(transferApiMock);

    when(transferApiMock.validateTaxonomyCategory(category, orgFiscalCode))
      .thenReturn(true);

    assertDoesNotThrow(
      () -> transferClient.validateTaxonomyCategory(category, orgFiscalCode, accessToken));
  }
}
