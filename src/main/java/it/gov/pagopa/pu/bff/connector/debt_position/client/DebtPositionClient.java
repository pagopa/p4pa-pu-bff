package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
public class DebtPositionClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, Boolean massive, String accessToken) {
    return debtPositionApisHolder.getDebtPositionApi(accessToken)
      .createDebtPosition(debtPositionDTO, massive);
  }

  public PagedModelDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, List<String> debtPositionOrigins, String operatorExternalId, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getDebtPositionViewSearchControllerApi(accessToken)
      .crudDebtPositionsViewFindDebtPositionViews(
        filtersDTO.getOrganizationId(),
        debtPositionOrigins,
        operatorExternalId,
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateFrom()),
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateTo()),
        filtersDTO.getFiscalCode(),
        filtersDTO.getDebtPositionTypeOrgId(),
        filtersDTO.getStatus(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }

  public DebtPositionDTO getDebtPosition(Long debtPositionId,
    String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionApi(accessToken)
        .getDebtPosition(debtPositionId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPosition with debtPositionId {} not found", debtPositionId);
      return null;
    }
  }

  public ResponseEntity<Void> deleteDebtPosition(Long debtPositionId, String accessToken){
    try {
      return debtPositionApisHolder.getDebtPositionApi(accessToken).deleteDebtPositionWithHttpInfo(debtPositionId);
    }catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("DebtPosition with ID %d not found".formatted(debtPositionId));
    }
  }

}

