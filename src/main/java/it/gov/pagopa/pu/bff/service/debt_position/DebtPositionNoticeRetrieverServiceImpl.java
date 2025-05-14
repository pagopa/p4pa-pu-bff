package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionNoticeRetrieverServiceImpl implements DebtPositionNoticeRetrieverService {
  private final PrintPaymentNoticeService printPaymentNoticeService;
  private final DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService;
  private final DebtPositionService debtPositionService;

  public DebtPositionNoticeRetrieverServiceImpl(
    PrintPaymentNoticeService printPaymentNoticeService,
    DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService,
    DebtPositionService debtPositionRetrieverService) {
    this.printPaymentNoticeService = printPaymentNoticeService;
    this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
    this.debtPositionService = debtPositionRetrieverService;
  }

  @Override
  public FileResourceDTO getNotice(Long organizationId, String iuv,
    Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
    DebtPositionDTO debtPosition = getDebtPosition(organizationId,
      debtPositionId, accessToken);
    validateOperator(loggedUser, accessToken, debtPosition);
    return printPaymentNoticeService.generateNotice(iuv,
      debtPosition,
      accessToken);
  }

  private void validateOperator(UserInfo loggedUser, String accessToken,
    DebtPositionDTO debtPosition) {
    DebtPositionTypeOrgOperators operator = debtPositionTypeOrgOperatorsService.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(
      debtPosition.getDebtPositionTypeOrgId(),
      loggedUser.getMappedExternalUserId(), accessToken);
    if(operator == null){
      throw new AuthorizationDeniedException("Access denied on debtPositionTypeOrgId " + debtPosition.getDebtPositionTypeOrgId() + " to user " + loggedUser.getMappedExternalUserId());
    }
  }

  private DebtPositionDTO getDebtPosition(Long organizationId,
    Long debtPositionId, String accessToken) {
    DebtPositionDTO debtPosition = debtPositionService.getDebtPosition(
      debtPositionId, accessToken);
    if(debtPosition==null){
      throw new ResourceNotFoundException(
        "DebtPosition having ID %d not found".formatted(debtPositionId));
    }
    if(!organizationId.equals(debtPosition.getOrganizationId())){
      throw new InvalidDebtPositionException(
        "The DebtPositionTypeOrg's organizationId "+ debtPosition.getOrganizationId()+
          " does not match the given organizationId "+ organizationId);
    }
    return debtPosition;
  }
}
