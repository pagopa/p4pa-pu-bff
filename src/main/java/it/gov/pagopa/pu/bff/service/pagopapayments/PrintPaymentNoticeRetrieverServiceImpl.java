package it.gov.pagopa.pu.bff.service.pagopapayments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import org.springframework.stereotype.Service;

@Service
public class PrintPaymentNoticeRetrieverServiceImpl implements PrintPaymentNoticeRetrieverService{
  private final PrintPaymentNoticeService printPaymentNoticeService;
  private final DebtPositionRetrieverService debtPositionRetrieverService;
  private final DebtPositionMapper debtPositionMapper;

  public PrintPaymentNoticeRetrieverServiceImpl(
    PrintPaymentNoticeService printPaymentNoticeService,
    DebtPositionRetrieverService debtPositionRetrieverService,
    DebtPositionMapper debtPositionMapper) {
    this.printPaymentNoticeService = printPaymentNoticeService;
    this.debtPositionRetrieverService = debtPositionRetrieverService;
    this.debtPositionMapper = debtPositionMapper;
  }

  @Override
  public FileResourceDTO generateNotice(Long organizationId, String iuv,
    Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
    DebtPositionDetailDTO debtPositionDetail = debtPositionRetrieverService.getDebtPositionDetail(
      debtPositionId, organizationId, loggedUser, accessToken);
    if(debtPositionDetail==null){
      throw new ResourceNotFoundException(
        "DebtPosition having ID %d not found".formatted(debtPositionId));
    }
    return printPaymentNoticeService.generateNotice(iuv,
      debtPositionMapper.mapToDebtPositionDTO(debtPositionDetail,organizationId, debtPositionId),
      accessToken);
  }
}
