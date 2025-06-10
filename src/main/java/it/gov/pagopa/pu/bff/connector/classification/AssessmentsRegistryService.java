package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.springframework.data.domain.Pageable;

public interface AssessmentsRegistryService {
  PagedModelAssessmentsRegistry findAssessmentsRegistriesByFilters(AssessmentsRegistryFiltersDTO filters,
                                                                   Pageable pageable, String accessToken);
}
