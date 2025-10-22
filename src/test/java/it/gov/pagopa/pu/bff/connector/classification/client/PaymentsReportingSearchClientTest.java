package it.gov.pagopa.pu.bff.connector.classification.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.CollectionModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingEmbedded;
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
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

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
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    PaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, paymentsReportingId,accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock,paymentsReportingSearchControllerApiMock);
  }

  @Test
  void whenGetPaymentsReportingThenReturnList() {
    Long organizationId = 1L;
    String iuf = "iuf123";
    String accessToken = "ACCESSTOKEN";

    List<PaymentsReporting> expectedResult = new ArrayList<>();
    expectedResult.add(new PaymentsReporting());

    PagedModelPaymentsReportingEmbedded pagedModelPaymentsReportingEmbeddedMock = Mockito.mock(PagedModelPaymentsReportingEmbedded.class);

    CollectionModelPaymentsReporting collectionModelPaymentsReportingMock = Mockito.mock(CollectionModelPaymentsReporting.class);

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);

    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindByOrganizationIdAndIuf(
      organizationId, iuf))
      .thenReturn(collectionModelPaymentsReportingMock);

    when(collectionModelPaymentsReportingMock.getEmbedded()).thenReturn(pagedModelPaymentsReportingEmbeddedMock);

    when(pagedModelPaymentsReportingEmbeddedMock.getPaymentsReportings()).thenReturn(expectedResult);

    List<PaymentsReporting> result = paymentsReportingSearchClient.getPaymentsReporting(organizationId, iuf, accessToken);

    assertSame(expectedResult, result);
  }
}
