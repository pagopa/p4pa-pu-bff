package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import org.springframework.data.domain.Pageable;

public interface ReceiptRetrieverService {

  PagedReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  ReceiptDetailDTO getReceiptDetail(Long organizationId, Long receiptId, UserInfo loggedUser, String accessToken);
  FileResourceDTO getReceiptPdf(Long organizationId, Long receiptId, UserInfo loggedUser, String accessToken);
}
