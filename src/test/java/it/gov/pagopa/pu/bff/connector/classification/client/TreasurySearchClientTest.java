package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.classification.client.generated.TreasurySearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import it.gov.pagopa.pu.classification.dto.generated.TreasuryOrigin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasurySearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private TreasurySearchControllerApi treasurySearchControllerApiMock;
  private TreasurySearchClient treasurySearchClient;

  @BeforeEach
  void setUp() {
    treasurySearchClient = new TreasurySearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      treasurySearchControllerApiMock
    );
  }

  @Test
  void whenGetTreasuryDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String treasuryId = "TREASURY123";
    Treasury expectedTreasury = Treasury.builder()
      .treasuryId(treasuryId)
      .organizationId(organizationId)
      .billYear("2025")
      .billCode("BILL123")
      .ingestionFlowFileId(100L)
      .billAmountCents(1000L)
      .billDate(LocalDate.now().minusDays(10))
      .pspLastName("PSPLastName")
      .orgBtCode("orgBtCode")
      .orgIstatCode("orgIstatCode")
      .treasuryOrigin(TreasuryOrigin.TREASURY_OPI)
      .build();

    when(classificationApisHolderMock.getTreasurySearchControllerApi(accessToken))
      .thenReturn(treasurySearchControllerApiMock);

    when(treasurySearchControllerApiMock.crudTreasuryFindByOrganizationIdAndTreasuryId(
      organizationId, treasuryId))
      .thenReturn(expectedTreasury);

    Treasury result = treasurySearchClient.getTreasuryDetail(organizationId, treasuryId, accessToken);

    assertSame(expectedTreasury, result);
  }

  @Test
  void whenGetTreasuryDetailNotFoundThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String treasuryId = "TREASURY123";

    when(classificationApisHolderMock.getTreasurySearchControllerApi(accessToken))
      .thenReturn(treasurySearchControllerApiMock);

    when(treasurySearchControllerApiMock.crudTreasuryFindByOrganizationIdAndTreasuryId(organizationId, treasuryId))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    Treasury result = treasurySearchClient.getTreasuryDetail(organizationId, treasuryId, accessToken);

    assertNull(result);
    verify(treasurySearchControllerApiMock).crudTreasuryFindByOrganizationIdAndTreasuryId(
      organizationId, treasuryId);
  }

}
