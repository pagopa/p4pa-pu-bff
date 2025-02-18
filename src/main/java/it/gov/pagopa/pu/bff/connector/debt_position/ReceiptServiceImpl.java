package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {

  private final ReceiptClient client;

  public ReceiptServiceImpl(ReceiptClient client) {
    this.client = client;
  }

  @Override
  public PagedModelReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, String accessToken) {
    return client.getReceipts(receiptViewFiltersDTO, pageable, accessToken);
  }
}
