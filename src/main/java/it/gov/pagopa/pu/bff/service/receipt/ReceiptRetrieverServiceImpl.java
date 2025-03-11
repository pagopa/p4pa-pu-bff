package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.ReceiptService;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.mapper.ReceiptDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReceiptRetrieverServiceImpl implements ReceiptRetrieverService {
  private final ReceiptService receiptService;
  private final ReceiptViewMapper receiptViewMapper;
  private final ReceiptDetailDTOMapper receiptDetailDTOMapper;

  public ReceiptRetrieverServiceImpl(ReceiptService receiptService, ReceiptViewMapper receiptViewMapper,
    ReceiptDetailDTOMapper receiptDetailDTOMapper) {
    this.receiptService = receiptService;
    this.receiptViewMapper = receiptViewMapper;
    this.receiptDetailDTOMapper = receiptDetailDTOMapper;
  }

  @Override
  public PagedReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(receiptViewFiltersDTO.getOrganizationId(), loggedUser);
    return receiptViewMapper.mapToPagedReceiptView(receiptService.getReceipts(receiptViewFiltersDTO, pageable, accessToken));
  }

  @Override
  public ReceiptDetailDTO getReceiptDetail(Long organizationId, Long receiptId,
    UserInfo loggedUser,
    String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return receiptDetailDTOMapper.mapToReceiptDetailDTO(receiptService.getReceiptDetail(receiptId,
      loggedUser.getMappedExternalUserId(), accessToken));
  }
}
