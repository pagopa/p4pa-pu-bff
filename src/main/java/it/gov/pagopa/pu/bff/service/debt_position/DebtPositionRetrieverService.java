package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import org.springframework.data.domain.Pageable;

public interface DebtPositionRetrieverService {
  DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, Boolean massive, UserInfo loggedUser, String accessToken);

  PagedDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  DebtPositionDetailDTO getDebtPositionDetail(Long debtPositionId, Long organizationId, UserInfo loggedUser, String accessToken);
}
