package it.gov.pagopa.pu.bff.service.receipts;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ReceiptViewServiceImpl implements ReceiptViewService {

  private final AuthorizationService authorizationService;
  private final ReceiptClient receiptClient;
  private final ReceiptViewMapper receiptViewMapper;

  public ReceiptViewServiceImpl(
    AuthorizationService authorizationService, ReceiptClient receiptClient, ReceiptViewMapper receiptViewMapper) {
    this.authorizationService = authorizationService;
    this.receiptClient = receiptClient;
    this.receiptViewMapper = receiptViewMapper;
  }

  @Override
  public PagedReceiptView getReceipts(
    Long organizationId,
    String receiptOrigin,
    String operatorExternalUserId,
    String iuv,
    String iur,
    String iud,
    Long debtPositionTypeOrgId,
    OffsetDateTime fromDate,
    OffsetDateTime toDate,
    Pageable pageable,
    UserInfo loggedUser,
    String accessToken) {

    authorizationService.validateAdminRole(organizationId, loggedUser);

    return receiptViewMapper.mapToPagedReceiptView(receiptClient.getReceipts(
      organizationId, receiptOrigin, operatorExternalUserId, iuv, iur, iud, debtPositionTypeOrgId, fromDate, toDate, pageable, accessToken));
  }

}
