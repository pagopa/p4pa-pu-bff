package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
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

  @Override
  public PagedModelAssessmentsDetail findPagedModelAssessmentsDetail(AssessmentsRowsDetailFiltersDTO filters, Pageable pageable, String accessToken) {
    return assessmentsClient.findPagedModelAssessmentsDetail(filters, pageable, accessToken);
  }
}
