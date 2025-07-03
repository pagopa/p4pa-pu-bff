package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ClassificationDetailDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;

@Service
public class ClassificationRetrieverServiceImpl implements ClassificationRetrieverService {

  private final ClassificationService classificationService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final ClassificationDetailDTOMapper classificationDetailDTOMapper;

  public ClassificationRetrieverServiceImpl(
          ClassificationService classificationService, DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService, ClassificationDetailDTOMapper classificationDetailDTOMapper) {
    this.classificationService = classificationService;
      this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
      this.classificationDetailDTOMapper = classificationDetailDTOMapper;
  }

  @Override
  public PagedTreasuredClassification getTreasuredClassification(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateTreasuredClassificationFilters(treasuredClassificationFiltersDTO,debtPositionTypeOrgCode);
    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)) {
      debtPositionTypeOrgRetrieverService.validateOperator(organizationId, debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
    } else {
      treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(getDebtPositionTypeOrgCodes(organizationId, loggedUser.getMappedExternalUserId(), accessToken));
    }

    return classificationService.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);
  }

  private Set<String> getDebtPositionTypeOrgCodes(Long organizationId, String mappedExternalUserId, String accessToken) {
    Set<String> debtPositionTypeOrgCodes = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgCodes(organizationId,mappedExternalUserId,accessToken);
    if(CollectionUtils.isEmpty(debtPositionTypeOrgCodes)){
      throw new ResourceNotFoundException("Classification not found for organizationId " + organizationId);
    }
    return debtPositionTypeOrgCodes;
  }

  private void validateTreasuredClassificationFilters(TreasuredClassificationFiltersDTO filters, String debtPositionTypeOrgCode) {
    if (filters.getLabel() == null &&
      StringUtils.isBlank(filters.getIud()) &&
      StringUtils.isBlank(filters.getIuv()) &&
      StringUtils.isBlank(filters.getIur()) &&
      DateUtils.isNullOrInvalidLocalDateRange(filters.getLastClassificationDate().getFrom(), filters.getLastClassificationDate().getTo()) &&
      DateUtils.isNullOrInvalidLocalDateRange(filters.getPayDate().getFrom(), filters.getPayDate().getTo()) &&
      DateUtils.isNullOrInvalidOffsetDateTimeRange(filters.getPaymentDateTime().getFrom(), filters.getPaymentDateTime().getTo()) &&
      DateUtils.isNullOrInvalidLocalDateRange(filters.getRegulationDate().getFrom(), filters.getRegulationDate().getTo()) &&
      DateUtils.isNullOrInvalidLocalDateRange(filters.getBillDate().getFrom(), filters.getBillDate().getTo()) &&
      DateUtils.isNullOrInvalidLocalDateRange(filters.getRegionValueDate().getFrom(), filters.getRegionValueDate().getTo()) &&
      StringUtils.isBlank(filters.getPspCompanyName()) &&
      StringUtils.isBlank(filters.getPspLastName()) &&
      StringUtils.isBlank(filters.getIuf()) &&
      StringUtils.isBlank(filters.getRegulationUniqueIdentifier()) &&
      StringUtils.isBlank(filters.getAccountRegistryCode()) &&
      filters.getBillAmountCents() == null &&
      StringUtils.isBlank(filters.getRemittanceInformation()) &&
      StringUtils.isBlank(filters.getDebtorFiscalCode()) &&
      StringUtils.isBlank(debtPositionTypeOrgCode) &&
      StringUtils.isBlank(filters.getBillYear()) &&
      StringUtils.isBlank(filters.getBillCode()) &&
      StringUtils.isBlank(filters.getDocumentYear()) &&
      StringUtils.isBlank(filters.getDocumentCode()) &&
      StringUtils.isBlank(filters.getProvisionalAe()) &&
      StringUtils.isBlank(filters.getProvisionalCode())) {

      throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
    }
  }

  @Override
  public ClassificationDetailDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    ClassificationDetailViewDTO classificationDetail = classificationService.getClassificationDetail(organizationId, classificationId, accessToken);
    if(classificationDetail!=null && StringUtils.isNotBlank(classificationDetail.getDebtPositionTypeOrgCode())){
      debtPositionTypeOrgRetrieverService.validateOperator(organizationId,classificationDetail.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
    }
    return classificationDetailDTOMapper.map(classificationDetail);
  }
}
