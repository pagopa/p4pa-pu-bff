package it.gov.pagopa.pu.bff.service.payments_reporting;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import org.springframework.data.domain.Pageable;

public interface PaymentsReportingRetrieverService {

  PagedPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                  Pageable pageable, UserInfo loggedUser, String accessToken);

  PagedPaymentsReportingRow getPaymentsReportingRows(Long organizationId, String iuf, String iuv, LocalDateIntervalFilter payDateFilter, Pageable pageable, UserInfo loggedUser, String accessToken);

  PaymentsReportingDetailDTO getPaymentsReportingDetail(Long organizationId, String iuf, String paymentsReportingId, UserInfo loggedUser, String accessToken);

}
