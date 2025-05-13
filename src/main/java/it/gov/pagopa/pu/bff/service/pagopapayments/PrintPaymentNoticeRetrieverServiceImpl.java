package it.gov.pagopa.pu.bff.service.pagopapayments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import org.springframework.stereotype.Service;

@Service
public class PrintPaymentNoticeRetrieverServiceImpl implements PrintPaymentNoticeRetrieverService{
  private final PrintPaymentNoticeService printPaymentNoticeService;

  public PrintPaymentNoticeRetrieverServiceImpl(
    PrintPaymentNoticeService printPaymentNoticeService) {
    this.printPaymentNoticeService = printPaymentNoticeService;
  }

  @Override
  public FileResourceDTO generateNotice(Long organizationId, String iuv,
    DebtPositionDTO debtPositionDTO, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
    if(!organizationId.equals(debtPositionDTO.getOrganizationId())){
      throw new InvalidDebtPositionException("The DebtPosition's organizationId "+ debtPositionDTO.getOrganizationId()+
        " does not match the given organizationId "+ organizationId);
    }
    return printPaymentNoticeService.generateNotice(organizationId,iuv,debtPositionDTO,accessToken);
  }
}
