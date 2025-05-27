package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;

public interface DebtPositionRetrieverService {
  DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, UserInfo loggedUser, String accessToken);

  PagedDebtPositionView getDebtPositionViews(DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  DebtPositionDetailDTO getDebtPositionDetail(Long debtPositionId, Long organizationId, UserInfo loggedUser, String accessToken);

  /**
   * Validates the user for the given organization and deletes the specified debt position.
   *
   * <p>Returns {@code true} if the debt position was physically deleted (HTTP 204 No Content),
   * or {@code false} if it was logically deleted by setting its status to <code>CANCELLED</code> (HTTP 200 OK).
   *
   * @param organizationId the ID of the organization
   * @param debtPositionId the ID of the debt position to delete
   * @param loggedUser the user performing the operation
   * @param accessToken the access token for authentication
   * @return {@code true} if physically deleted, {@code false} if logically deleted
   */
  boolean deleteDebtPosition(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken);

  Resource getDebtPositionNoticesZip(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken);

  void validateOperator(Long debtPositionId, Long organizationId, UserInfo loggedUser, String accessToken);
}
