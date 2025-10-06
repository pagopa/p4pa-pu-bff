package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClassificationSearchClient {
  private final ClassificationApisHolder classificationApisHolder;

  public ClassificationSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelClassification getClassifications(Long organizationId, ClassificationFiltersDTO filters, Pageable pageable, String accessToken) {
    return classificationApisHolder.getClassificationSearchControllerApi(accessToken)
      .crudClassificationsFindByFilters(organizationId,
        filters.getIuv(),
        filters.getDebtPositionTypeOrgCodes(),
        filters.getLabels(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
