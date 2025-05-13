package it.gov.pagopa.pu.bff.connector.pagopapayments.client;

import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.pagopapayments.config.PagoPAPaymentsApisHolder;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.pagopapayments.controller.generated.PrintPaymentNoticeApi;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PrintPaymentNoticeClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PagoPAPaymentsApisHolder pagoPAPaymentsApisHolderMock;
  @Mock
  private PrintPaymentNoticeApi printPaymentNoticeApiMock;

  private PrintPaymentNoticeClient printPaymentNoticeClient;

  @BeforeEach
  void setUp() {
    printPaymentNoticeClient = new PrintPaymentNoticeClient(pagoPAPaymentsApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(pagoPAPaymentsApisHolderMock);
  }

  @Test
  void whenGenerateNoticeThenOk() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    ByteArrayResource expectedResource = new ByteArrayResource("PDF-DATA".getBytes());
    String expectedFileName = "filename";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(
      ContentDisposition.attachment().filename(expectedFileName).build());
    ResponseEntity<Resource> responseEntity = new ResponseEntity<>(expectedResource, headers, HttpStatus.OK);

    when(pagoPAPaymentsApisHolderMock.getPrintPaymentNoticeControllerApi(accessToken))
      .thenReturn(printPaymentNoticeApiMock);
    when(printPaymentNoticeApiMock.generateNoticeWithHttpInfo(organizationId,iuv,debtPositionDTO)).thenReturn(
      responseEntity);

    FileResourceDTO response = printPaymentNoticeClient.generateNotice(
      organizationId, iuv, debtPositionDTO, accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResource,response.getResource());
    Assertions.assertEquals(expectedFileName,response.getFileName());
  }
}
