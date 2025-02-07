package it.gov.pagopa.pu.bff.service.receipts;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
import org.springframework.data.domain.Pageable;

public interface ReceiptViewService {

  PagedReceiptView getReceipts(ReceiptFilterDTO filter, Pageable pageable, UserInfo loggedUser, String accessToken);

}
