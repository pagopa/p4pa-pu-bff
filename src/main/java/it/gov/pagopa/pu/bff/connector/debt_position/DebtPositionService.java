package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DebtPositionService {
  DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, boolean massive, String accessToken);

  PagedModelDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, List<String> debtPositionOrigins, String operatorExternalId, Pageable pageable, String accessToken);

  DebtPositionDTO getDebtPosition(Long debtPositionId, String accessToken);
  PagedModelDebtPosition getDebtPositionByDebtPositionTypeOrgId(Long debtPositionTypeOrgId, Pageable pageable, String accessToken);
}
