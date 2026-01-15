package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;

public interface PaymentsReportingService {

  PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId,
    String iuf, String regulationUniqueIdentifier,
    LocalDateIntervalFilter regulationDateFilter, String iuv,
    Pageable pageable, String accessToken);

  PagedModelPaymentsReportingWithReceiptView getPaymentsReportingRows(Long organizationId,
                                                                      String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
                                                                      String debtPositionTypeOrgCode, String debtorFiscalCode,
                                                                      Pageable pageable, String accessToken);

  PaymentsReporting getPaymentsReportingDetail(Long organizationId,
    String paymentsReportingId, String accessToken);

}
