package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import org.springframework.data.domain.Pageable;

public interface ReceiptRetrieverService {

  PagedReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

}
