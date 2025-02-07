package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class ReceiptClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public ReceiptClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelReceiptView getReceipts(ReceiptFilterDTO filter, Pageable pageable, String accessToken) {
    try {
      return debtPositionApisHolder.getReceiptViewSearchControllerApi(accessToken)
        .crudReceiptsViewFindReceiptsByFilters(
          String.valueOf(filter.getOrganizationId()),
          filter.getReceiptOrigin(),
          filter.getOperatorExternalUserId(),
          filter.getIuv(),
          filter.getIur(),
          filter.getIud(),
          filter.getDebtPositionTypeOrgId(),
          filter.getFromDate(),
          filter.getToDate(),
          PageUtils.getPageNumber(pageable),
          PageUtils.getPageSize(pageable),
          PageUtils.getSortList(pageable));
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("Receipts for organizationId {} not found", filter.getOrganizationId());
        return null;
      }
      log.error("Error retrieving receipts for organizationId: {}", filter.getOrganizationId(), e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving receipts for organizationId: {}", filter.getOrganizationId(), e);
      throw e;
    }
  }

}


