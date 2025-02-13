package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {
  private final ReceiptClient receiptClient;
  private final ReceiptViewMapper receiptViewMapper;

  public ReceiptServiceImpl(ReceiptClient receiptClient, ReceiptViewMapper receiptViewMapper) {
    this.receiptClient = receiptClient;
    this.receiptViewMapper = receiptViewMapper;
  }

  @Override
  public PagedReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(receiptViewFiltersDTO.getOrganizationId(), loggedUser);
    return receiptViewMapper.mapToPagedReceiptView(receiptClient.getReceipts(receiptViewFiltersDTO, pageable, accessToken));
  }

}
