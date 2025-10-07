package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.*;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ClassificationDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.TreasuredClassificationExtendedDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassificationRetrieverServiceImpl implements ClassificationRetrieverService {

  private final ClassificationService classificationService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final ClassificationDetailDTOMapper classificationDetailDTOMapper;
  private final AssessmentsService assessmentsService;
  private final TreasuredClassificationExtendedDTOMapper treasuredClassificationExtendedDTOMapper;
  private final OrganizationService organizationService;
  private final Integer pageMaxSize;

  public ClassificationRetrieverServiceImpl(
    ClassificationService classificationService,
    DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService,
    ClassificationDetailDTOMapper classificationDetailDTOMapper,
    AssessmentsService assessmentsService,
    TreasuredClassificationExtendedDTOMapper treasuredClassificationExtendedDTOMapper,
    OrganizationService organizationService,
    @Value("${rest.page.request-max-page-size}") Integer pageMaxSize) {
    this.classificationService = classificationService;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.classificationDetailDTOMapper = classificationDetailDTOMapper;
    this.assessmentsService = assessmentsService;
    this.treasuredClassificationExtendedDTOMapper = treasuredClassificationExtendedDTOMapper;
    this.organizationService = organizationService;
    this.pageMaxSize = pageMaxSize;
  }

  @Override
  public PagedTreasuredClassificationExtendedDTO getTreasuredClassification(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    validateTreasuredClassificationFilters(treasuredClassificationFiltersDTO, debtPositionTypeOrgCode);

    if (StringUtils.isNotBlank(debtPositionTypeOrgCode)) {
      debtPositionTypeOrgRetrieverService.validateOperator(organizationId, debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
    } else {
      treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(getDebtPositionTypeOrgCodes(organizationId, loggedUser.getMappedExternalUserId(), accessToken));
    }

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if(organization == null) {
      throw new ResourceNotFoundException("Organization having ID " + organizationId + " not found");
    }
    treasuredClassificationFiltersDTO.setExcludedLabels(getExcludedLabels(organization));

    return treasuredClassificationExtendedDTOMapper.map(
      classificationService.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken), organization);
  }

  private static Set<String> getExcludedLabels(Organization organization) {
    Set<String> excludedLabels = new HashSet<>();
    if (Boolean.FALSE.equals(organization.getFlagPaymentNotification())) {
      excludedLabels.add(ClassificationsEnum.RT_NO_IUD.getValue());
      excludedLabels.add(ClassificationsEnum.IUD_NO_RT.getValue());
    }
    if (Boolean.FALSE.equals(organization.getFlagTreasury())) {
      excludedLabels.add(ClassificationsEnum.RT_TES.getValue());
      excludedLabels.add(ClassificationsEnum.RT_IUF_TES.getValue());
      excludedLabels.add(ClassificationsEnum.IUF_NO_TES.getValue());
      excludedLabels.add(ClassificationsEnum.TES_NO_IUF_OR_IUV.getValue());
      excludedLabels.add(ClassificationsEnum.IUF_TES_DIV_IMP.getValue());
      excludedLabels.add(ClassificationsEnum.TES_NO_MATCH.getValue());
    }
    return excludedLabels;
  }

  private Set<String> getDebtPositionTypeOrgCodes(Long organizationId, String mappedExternalUserId, String accessToken) {
    Set<String> debtPositionTypeOrgCodes = debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgCodes(organizationId, null, mappedExternalUserId, accessToken);
    if (CollectionUtils.isEmpty(debtPositionTypeOrgCodes)) {
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

    if (classificationDetail == null) {
      return null;
    }

    if (StringUtils.isNotBlank(classificationDetail.getDebtPositionTypeOrgCode())) {
      debtPositionTypeOrgRetrieverService.validateOperator(organizationId, classificationDetail.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
    }

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if (organization == null) {
      throw new ResourceNotFoundException("Organization having ID " + organizationId + " not found");
    }

    return classificationDetailDTOMapper.map(classificationDetail, organization);
  }

  @Override
  public PagedClassificationPaidInstallmentsView getPaidInstallments(
    Long organizationId, Long assessmentId, ClassificationPaidInstallmentsFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {

    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    validatePaidInstallmentsFilters(filters.getIuv(), filters.getPaymentDateTimeIntervalFilter(), filters.getReceiptCreationDateInterval());

    if(assessmentId!=null) {
      filters.setIuds(getIudsFilter(organizationId, assessmentId, accessToken));
    }
    return classificationService.getPaidInstallments(organizationId, filters, pageable, accessToken);
  }

  private Set<String> getIudsFilter(Long organizationId, Long assessmentId, String accessToken) {
    Assessments assessment = assessmentsService.getAssessmentsById(assessmentId, accessToken);
    if (assessment == null || !assessment.getOrganizationId().equals(organizationId)) {
      throw new ResourceNotFoundException("Assessment with id " + assessmentId + " not found");
    }
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = getAssessmentsRowsDetailFiltersDTO(assessmentId);
    Pageable maxPageable = PageRequest.of(0, pageMaxSize);
    PagedModelAssessmentsDetail assessmentsDetailPage = assessmentsService.findPagedModelAssessmentsDetail(
            assessmentsRowsDetailFiltersDTO, maxPageable, accessToken);
    return extractIuds(assessmentsDetailPage);
  }

  private void validatePaidInstallmentsFilters(String iuv, OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter, OffsetDateTimeIntervalFilter receiptCreationDateTimeInterval) {
    if (paymentDateTimeIntervalFilter != null) {
      DateUtils.validateDateFilters(paymentDateTimeIntervalFilter,"paymentDateTime");
    }
    if (receiptCreationDateTimeInterval != null) {
      DateUtils.validateDateFilters(receiptCreationDateTimeInterval,"receiptCreationDate");
    }
    if (StringUtils.isBlank(iuv) &&
      (paymentDateTimeIntervalFilter == null || DateUtils.isNullOrInvalidOffsetDateTimeRange(paymentDateTimeIntervalFilter.getFrom(), paymentDateTimeIntervalFilter.getTo())) &&
      (receiptCreationDateTimeInterval == null || DateUtils.isNullOrInvalidOffsetDateTimeRange(receiptCreationDateTimeInterval.getFrom(), receiptCreationDateTimeInterval.getTo()))) {
      throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
    }
  }

  private static AssessmentsRowsDetailFiltersDTO getAssessmentsRowsDetailFiltersDTO(Long assessmentId) {
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = new AssessmentsRowsDetailFiltersDTO();
    assessmentsRowsDetailFiltersDTO.setAssessmentId(assessmentId);
    return assessmentsRowsDetailFiltersDTO;
  }

  private Set<String> extractIuds(PagedModelAssessmentsDetail page) {
    return Optional.ofNullable(page.getEmbedded())
      .map(PagedModelAssessmentsDetailEmbedded::getAssessmentsDetails)
      .orElse(List.of())
      .stream()
      .map(AssessmentsDetail::getIud)
      .collect(Collectors.toSet());
  }

  @Override
  public PagedModelClassification getClassifications(
    Long organizationId, ClassificationFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    return classificationService.getClassifications(organizationId, filters, pageable, accessToken);
  }
}
