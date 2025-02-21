package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import org.springframework.data.domain.Pageable;

public interface DebtPositionRetrieverService {
  PagedDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
}
