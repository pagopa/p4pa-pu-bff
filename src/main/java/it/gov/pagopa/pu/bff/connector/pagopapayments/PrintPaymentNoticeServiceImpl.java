package it.gov.pagopa.pu.bff.connector.pagopapayments;

import it.gov.pagopa.pu.bff.connector.pagopapayments.client.PrintPaymentNoticeClient;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import org.springframework.stereotype.Service;

@Service
public class PrintPaymentNoticeServiceImpl implements PrintPaymentNoticeService {

  private final PrintPaymentNoticeClient client;

  public PrintPaymentNoticeServiceImpl(PrintPaymentNoticeClient client) {
    this.client = client;
  }

  @Override
  public FileResourceDTO generateNotice(String iuv,
    DebtPositionDTO debtPositionDTO, String accessToken) {
    return client.generateNotice(iuv, debtPositionDTO, accessToken);
  }
}
