package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import org.springframework.data.domain.Pageable;

public interface PaymentsReportingService {

  PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                       Pageable pageable, String accessToken);

}
