package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssessmentsClient {

  private final ClassificationApisHolder classificationApisHolder;

  public AssessmentsClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken){
    return classificationApisHolder.getAssessmentsControllerApi(accessToken)
      .getPagedAssessmentsList(
        filters.getAssessmentName(),
        filters.getUpdateDateFrom(),
        filters.getUpdateDateTo(),
        filters.getIuv(),
        filters.getDebtPositionTypeOrgCodes().stream().toList(),
        filters.getStatus(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
