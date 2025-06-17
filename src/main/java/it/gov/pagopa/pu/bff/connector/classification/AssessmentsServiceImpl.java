package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssessmentsServiceImpl implements AssessmentsService{

  private final AssessmentsClient assessmentsClient;

  public AssessmentsServiceImpl(AssessmentsClient assessmentsClient) {
    this.assessmentsClient = assessmentsClient;
  }

  @Override
  public PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken) {
    return assessmentsClient.findPagedAssessmentsView(filters, pageable, accessToken);
  }

}
