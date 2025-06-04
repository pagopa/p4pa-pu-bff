package it.gov.pagopa.pu.bff.service.receipt;

import freemarker.template.TemplateException;
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
import it.gov.pagopa.pu.bff.util.DocumentComposition;
import it.gov.pagopa.pu.bff.util.Utilities;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptRetrieverServiceImpl implements ReceiptRetrieverService {
  private final ReceiptService receiptService;
  private final OrganizationService organizationService;
  private final DocumentComposition documentComposition;
  private final ReceiptViewMapper receiptViewMapper;
  private final ReceiptDetailDTOMapper receiptDetailDTOMapper;
  public static final String RECEIPT_LOGO = "logo";
  public static final String RECEIPT_ORG_NAME = "orgName";
  public static final String RECEIPT_IUV = "iuv";
  public static final String RECEIPT_DEBTOR_NAME = "debtorName";
  public static final String RECEIPT_DEBTOR_FISCAL_CODE = "debtorFiscalCode";
  public static final String RECEIPT_TOTAL_AMOUNT = "totalAmount";
  public static final String RECEIPT_PAYMENT_DATE = "paymentDate";
  public static final String RECEIPT_PSP_NAME = "pspName";
  public static final String RECEIPT_FEE_AMOUNT = "feeAmount";

  public ReceiptRetrieverServiceImpl(ReceiptService receiptService, OrganizationService organizationService, DocumentComposition documentComposition, ReceiptViewMapper receiptViewMapper,
                                     ReceiptDetailDTOMapper receiptDetailDTOMapper) {
    this.receiptService = receiptService;
    this.organizationService = organizationService;
      this.documentComposition = documentComposition;
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
    if (filtersDTO.getReceiptOrigin() == null &&
      StringUtils.isBlank(filtersDTO.getIuv()) &&
      StringUtils.isBlank(filtersDTO.getIur()) &&
      StringUtils.isBlank(filtersDTO.getIud()) &&
      filtersDTO.getDebtPositionTypeOrgId() == null &&
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
            new ByteArrayResource(generateReceiptPdf(receiptDetail, organization)),
            "RECEIPT_"+organization.getOrgFiscalCode()+"_"+receiptId
    );
  }

  private byte[] generateReceiptPdf(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail, Organization organization) {
    byte[] receiptPdf;
    try {
       receiptPdf = documentComposition.executePdfTemplate(DocumentComposition.TemplateType.RECEIPT, buildTemplateModel(receiptDetail, organization));
    } catch (IOException | TemplateException e) {
        throw new IllegalStateException(e);
    }
    return receiptPdf;
  }

  private Map<String, Object> buildTemplateModel(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail, Organization organization) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put(RECEIPT_LOGO,StringUtils.defaultString(organization.getOrgLogo()));
    templateModel.put(RECEIPT_ORG_NAME,organization.getOrgName());
    templateModel.put(RECEIPT_IUV,StringUtils.defaultString(receiptDetail.getIuv()));
    templateModel.put(RECEIPT_DEBTOR_NAME,receiptDetail.getDebtor().getFullName());
    templateModel.put(RECEIPT_DEBTOR_FISCAL_CODE,receiptDetail.getDebtor().getFiscalCode());
    templateModel.put(RECEIPT_TOTAL_AMOUNT,Utilities.formatPrice(receiptDetail.getPaymentAmountCents()));
    templateModel.put(RECEIPT_PAYMENT_DATE,receiptDetail.getPaymentDateTime()!=null?receiptDetail.getPaymentDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):"");
    templateModel.put(RECEIPT_PSP_NAME,receiptDetail.getPspCompanyName());
    templateModel.put(RECEIPT_FEE_AMOUNT, Utilities.formatPrice(receiptDetail.getFeeCents()));
    return templateModel;
  }
}
