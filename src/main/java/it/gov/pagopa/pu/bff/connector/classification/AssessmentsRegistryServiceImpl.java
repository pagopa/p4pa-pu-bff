package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsRegistryClient;
import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService {

  private final AssessmentsRegistrySearchClient assessmentsRegistrySearchClient;
  private final AssessmentsRegistryClient assessmentsRegistryClient;

  public AssessmentsRegistryServiceImpl(AssessmentsRegistrySearchClient assessmentsRegistrySearchClient, AssessmentsRegistryClient assessmentsRegistryClient) {
    this.assessmentsRegistrySearchClient = assessmentsRegistrySearchClient;
    this.assessmentsRegistryClient = assessmentsRegistryClient;
  }

  @Override
  public PagedModelAssessmentsRegistry findAssessmentsRegistriesByFilters(AssessmentsRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return assessmentsRegistrySearchClient.findAssessmentsRegistriesByFilters(filters, pageable, accessToken);
  }

  @Override
  public AssessmentsRegistry getAssessmentsRegistry(Long assessmentRegistryId, String accessToken) {
    return assessmentsRegistrySearchClient.getAssessmentsRegistry(assessmentRegistryId, accessToken);
  }

  @Override
  public AssessmentsRegistry createAssessmentsRegistry(AssessmentsRegistry assessmentsRegistry, String accessToken) {
    return assessmentsRegistryClient.createAssessmentsRegistry(assessmentsRegistry, accessToken);
  }

  @Override
  public AssessmentsRegistry updateAssessmentsRegistry(AssessmentsRegistry body, String accessToken) {
    return assessmentsRegistryClient.updateAssessmentsRegistry(body, accessToken);
  }
}
