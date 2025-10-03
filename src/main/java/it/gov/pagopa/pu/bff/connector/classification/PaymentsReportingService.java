package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;

public interface PaymentsReportingService {

  PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId,
    String iuf, String regulationUniqueIdentifier,
    LocalDateIntervalFilter regulationDateFilter, String iuv,
    Pageable pageable, String accessToken);

  PagedModelPaymentsReporting getPaymentsReportingRows(Long organizationId,
    String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
    Pageable pageable, String accessToken);

  PaymentsReporting getPaymentsReportingDetail(Long organizationId,
    String paymentsReportingId, String accessToken);

}
