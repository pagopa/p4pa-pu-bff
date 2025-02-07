package it.gov.pagopa.pu.bff.service.receipts;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface ReceiptViewService {

  @SuppressWarnings("squid:S107")
  PagedReceiptView getReceipts(
    Long organizationId,
    String receiptOrigin,
    String operatorExternalUserId,
    String iuv,
    String iur,
    String iud,
    Long debtPositionTypeOrgId,
    OffsetDateTime fromDate,
    OffsetDateTime toDate,
    Pageable pageable,
    UserInfo loggedUser,
    String accessToken);

}
