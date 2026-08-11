package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.classification.client.generated.PaymentsReportingSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingSearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private PaymentsReportingSearchControllerApi paymentsReportingSearchControllerApiMock;

  private PaymentsReportingSearchClient paymentsReportingSearchClient;

  @BeforeEach
  void setUp() {
    paymentsReportingSearchClient = new PaymentsReportingSearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      paymentsReportingSearchControllerApiMock
    );
  }

  @Test
  void whenGetPaymentsReportingDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PaymentsReporting expectedResult = new PaymentsReporting();

    Long organizationId = 1L;
    String paymentsReportingId = "PAYREP123";

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);

    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(
      organizationId, paymentsReportingId))
      .thenReturn(expectedResult);

    PaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken);

    assertSame(expectedResult, result);
  }


  @Test
  void givenNoPaymentsReportingWhenGetDebtPositionPaymentsReportingDetailThenReturnNull() {
    Long organizationId = 1L;
    String paymentsReportingId = "PAYREP123";
    String accessToken = "ACCESSTOKEN";

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);
    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(organizationId, paymentsReportingId))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    PaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, paymentsReportingId,accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock,paymentsReportingSearchControllerApiMock);
  }
}
