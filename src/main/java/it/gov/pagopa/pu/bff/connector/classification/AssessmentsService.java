package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.springframework.data.domain.Pageable;

public interface AssessmentsService {
  PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken);
}
