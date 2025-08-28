package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.ManageDebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
public class DebtPositionClient {

  public static final String DEBT_POSITION_NOT_FOUND = "DebtPosition with ID %d not found";
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
        operatorExternalId,
        debtPositionOrigins,
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateTimeFrom()),
        DateUtils.toLocalDateTime(filtersDTO.getCreationDateTimeTo()),
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


  /**
   * Deletes a debt position identified by its ID.
   * This method attempts to delete the debt position using the provided access token.
   * The deletion can result in different outcomes based on the HTTP response status:
   *
   * <ul>
   *    <li><b>true</b> - The debt position was physically deleted from the database (HTTP 204 No Content).</li>
   *    <li><b>false</b> - The debt position was not physically deleted, but its status was changed to <code>CANCELLED</code> (HTTP 200 OK).</li>
   *    <li>If an exception is thrown, such as {@link HttpClientErrorException.NotFound}, it indicates that the debt position was not found,
   *     and a {@link ResourceNotFoundException} is raised.</li>
   * </ul>
   *
   * @param debtPositionId the ID of the debt position to delete
   * @param accessToken the access token used for authentication
   * @return {@code true} if the debt position was physically deleted, {@code false} if its status was changed to CANCELLED
   * @throws ResourceNotFoundException if the debt position with the given ID does not exist
   */
  public boolean deleteDebtPosition(Long debtPositionId, String accessToken){
    try {
      ResponseEntity<Void> voidResponseEntity = debtPositionApisHolder.getDebtPositionApi(accessToken).deleteDebtPositionWithHttpInfo(debtPositionId);
      return voidResponseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT);
    }catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException(DEBT_POSITION_NOT_FOUND.formatted(debtPositionId));
    }
  }

  public DebtPositionDTO manageDebtPositionInstallments(Long debtPositionId, ManageDebtPositionDTO manageDebtPositionDTO, String accessToken){
    try {
      return debtPositionApisHolder.getDebtPositionApi(accessToken).manageDebtPositionInstallments(debtPositionId, manageDebtPositionDTO);
    }catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException(DEBT_POSITION_NOT_FOUND.formatted(debtPositionId));
    }
  }

  public DebtPositionDTO publishDebtPosition(Long debtPositionId, String accessToken){
    try {
      return debtPositionApisHolder.getDebtPositionApi(accessToken).publishDebtPosition(debtPositionId);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException(DEBT_POSITION_NOT_FOUND.formatted(debtPositionId));
    } catch (HttpClientErrorException.Conflict e) {
      throw new ConflictException("Conflict detected publishing DebtPosition with ID %d".formatted(debtPositionId));
    }
  }
}

