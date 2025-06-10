package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssessmentsRegistrySearchClient {
  private final ClassificationApisHolder classificationApisHolder;

  public AssessmentsRegistrySearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelAssessmentsRegistry findAssessmentsRegistriesByFilters(AssessmentsRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return classificationApisHolder.getAssessmentsRegistrySearchControllerApi(accessToken)
      .crudAssessmentsRegistriesFindAssessmentsRegistriesByFilters(
              filters.getOrganizationId(),
        filters.getDebtPositionTypeOrgCodes(),
        filters.getSectionCode(),
        filters.getSectionDescription(),
        filters.getOfficeCode(),
        filters.getOfficeDescription(),
        filters.getAssessmentCode(),
        filters.getAssessmentDescription(),
        filters.getOperatingYear(),
        filters.getStatus(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
