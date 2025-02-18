package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import org.springframework.data.domain.Pageable;

public interface ReceiptService {
  PagedModelReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, String accessToken);
}
