package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import org.springframework.data.domain.Pageable;

public interface ReceiptService {
  PagedModelReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, String accessToken);
  ReceiptDetailDTO getReceiptDetail(Long receiptId, String operatorExternalUserId, Long organizationId, String accessToken);
  FileResourceDTO getReceiptPdf(Long receiptId, Long organizationId, String accessToken);
}
