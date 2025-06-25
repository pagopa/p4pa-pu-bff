package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassificationRetrieverServiceImpl implements ClassificationRetrieverService {

  private final ClassificationService classificationService;

  public ClassificationRetrieverServiceImpl(
    ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public PagedTreasuredClassification getTreasuredClassification(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    validateTreasuredClassificationFilters(treasuredClassificationFiltersDTO);

    return classificationService.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);
  }

  private void validateTreasuredClassificationFilters(TreasuredClassificationFiltersDTO filters) {
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
      StringUtils.isBlank(filters.getDebtPositionTypeOrgCode()) &&
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
  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return classificationService.getClassificationDetail(organizationId, classificationId, accessToken);
  }
}
