package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.ReceiptService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ReceiptDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReceiptRetrieverServiceImpl implements ReceiptRetrieverService {
  private final ReceiptService receiptService;
  private final OrganizationService organizationService;
  private final ReceiptFileService receiptFileService;
  private final ReceiptViewMapper receiptViewMapper;
  private final ReceiptDetailDTOMapper receiptDetailDTOMapper;

  public ReceiptRetrieverServiceImpl(ReceiptService receiptService, OrganizationService organizationService, ReceiptFileService receiptFileService, ReceiptViewMapper receiptViewMapper,
                                     ReceiptDetailDTOMapper receiptDetailDTOMapper) {
    this.receiptService = receiptService;
    this.organizationService = organizationService;
      this.receiptFileService = receiptFileService;
      this.receiptViewMapper = receiptViewMapper;
    this.receiptDetailDTOMapper = receiptDetailDTOMapper;
  }

  @Override
  public PagedReceiptView getReceipts(ReceiptViewFiltersDTO receiptViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(receiptViewFiltersDTO.getOrganizationId(), loggedUser);

    validateReceiptViewFilters(receiptViewFiltersDTO);

    return receiptViewMapper.mapToPagedReceiptView(
      receiptService.getReceipts(receiptViewFiltersDTO, pageable, accessToken));
  }

  private void validateReceiptViewFilters(ReceiptViewFiltersDTO filtersDTO) {
    if ((filtersDTO.getReceiptOrigins() == null || filtersDTO.getReceiptOrigins().isEmpty()) &&
      StringUtils.isBlank(filtersDTO.getIuv()) &&
      StringUtils.isBlank(filtersDTO.getIur()) &&
      StringUtils.isBlank(filtersDTO.getIud()) &&
      filtersDTO.getDebtPositionTypeOrgId() == null &&
      StringUtils.isBlank(filtersDTO.getFiscalCode()) &&
      (filtersDTO.getPaymentDateTime() == null ||
        DateUtils.isNullOrInvalidOffsetDateTimeRange(filtersDTO.getPaymentDateTime().getFrom(), filtersDTO.getPaymentDateTime().getTo()))) {
      throw new IllegalArgumentException("At least one of the research fields must be provided, and both 'from' and 'to' payment dates must be set together");
    }
  }

  @Override
  public ReceiptDetailDTO getReceiptDetail(Long organizationId, Long receiptId,
                                           UserInfo loggedUser,
                                           String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return receiptDetailDTOMapper.mapToReceiptDetailDTO(receiptService.getReceiptDetail(receiptId,
      loggedUser.getMappedExternalUserId(), accessToken));
  }

  @Override
  public FileResourceDTO getReceiptPdf(Long organizationId, Long receiptId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail = receiptService.getReceiptDetail(receiptId,
            loggedUser.getMappedExternalUserId(), accessToken);
    if(receiptDetail==null){
      throw new ResourceNotFoundException("Receipt with ID "+receiptId+" not found");
    }
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if(organization==null){
      throw new ResourceNotFoundException("Organization with ID "+organizationId+" not found");
    }
    return new FileResourceDTO(
            new ByteArrayResource(receiptFileService.generateReceiptPdf(receiptDetail, organization)),
            "RECEIPT_"+organization.getOrgFiscalCode()+"_"+receiptId+".pdf"
    );
  }
}
