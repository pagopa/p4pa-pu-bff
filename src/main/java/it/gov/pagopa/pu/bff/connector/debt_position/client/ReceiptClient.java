package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class ReceiptClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public ReceiptClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  @SuppressWarnings("squid:S107")
  public PagedModelReceiptView getReceipts(
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
    String accessToken) {

    try {
      return debtPositionApisHolder.getReceiptViewSearchControllerApi(accessToken)
        .crudReceiptsViewFindReceiptsByFilters(
          String.valueOf(organizationId), receiptOrigin, operatorExternalUserId,
          iuv, iur, iud, debtPositionTypeOrgId, fromDate, toDate,
          PageUtils.getPageNumber(pageable),
          PageUtils.getPageSize(pageable),
          PageUtils.getSortList(pageable));
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("Receipts for organizationId {} not found", organizationId);
        return null;
      }
      log.error("Error retrieving receipts for organizationId: {}", organizationId, e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving receipts for organizationId: {}", organizationId, e);
      throw e;
    }
  }

}


