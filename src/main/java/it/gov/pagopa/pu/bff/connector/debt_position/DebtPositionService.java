package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DebtPositionService {
  DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, boolean massive, String accessToken);

  PagedModelDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, List<String> debtPositionOrigins, String operatorExternalId, Pageable pageable, String accessToken);

  DebtPositionDTO getDebtPosition(Long debtPositionId, String accessToken);
  PagedModelDebtPosition getDebtPositionByDebtPositionTypeOrgId(Long debtPositionTypeOrgId, Pageable pageable, String accessToken);


  /**
   * Delegates the deletion of a debt position to the client.
   *
   * <p>Returns {@code true} if the debt position was physically deleted (HTTP 204 No Content),
   * or {@code false} if it was logically deleted by setting its status to <code>CANCELLED</code> (HTTP 200 OK).
   *
   * @param debtPositionId the ID of the debt position
   * @param accessToken the access token for authentication
   * @return {@code true} if physically deleted, {@code false} if logically deleted
   */
  boolean deleteDebtPosition(Long debtPositionId, String accessToken);

  boolean hasOperatorGrantOnDebtPosition(Long debtPositionId, Long organizationId, String operatorExternalUserId, String accessToken);
}
