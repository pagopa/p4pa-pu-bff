package it.gov.pagopa.pu.bff.connector.pagopapayments;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.pagopapayments.client.PrintPaymentNoticeClient;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PrintPaymentNoticeServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  @Mock
  private PrintPaymentNoticeClient printPaymentNoticeClientMock;

  private PrintPaymentNoticeService service;

  @BeforeEach
  void setUp() {
    service = new PrintPaymentNoticeServiceImpl(printPaymentNoticeClientMock);
  }

  @Test
  void whenGenerateNoticeThenInvokeClient() {
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    FileResourceDTO expectedResult = new FileResourceDTO();

    when(printPaymentNoticeClientMock.generateNotice(iuv,debtPositionDTO,
      ACCESS_TOKEN))
      .thenReturn(expectedResult);

    FileResourceDTO result = service.generateNotice(iuv,debtPositionDTO,
      ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
