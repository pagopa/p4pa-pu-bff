package it.gov.pagopa.pu.bff.service.receipts;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public PagedReceiptView getReceipts(ReceiptFilterDTO filter, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(filter.getOrganizationId(), loggedUser);
    return receiptViewMapper.mapToPagedReceiptView(receiptClient.getReceipts(filter, pageable, accessToken));
  }

}
