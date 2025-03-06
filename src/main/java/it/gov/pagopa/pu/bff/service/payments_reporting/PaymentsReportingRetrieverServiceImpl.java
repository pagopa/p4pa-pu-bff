package it.gov.pagopa.pu.bff.service.payments_reporting;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingRetrieverServiceImpl implements PaymentsReportingRetrieverService {

  private final PaymentsReportingService paymentsReportingService;
  private final PaymentsReportingViewMapper paymentsReportingViewMapper;
  private final PaymentsReportingMapper paymentsReportingMapper;

  public PaymentsReportingRetrieverServiceImpl(PaymentsReportingService paymentsReportingService,
                                               PaymentsReportingViewMapper paymentsReportingViewMapper,
    PaymentsReportingMapper paymentsReportingMapper) {
    this.paymentsReportingService = paymentsReportingService;
    this.paymentsReportingViewMapper = paymentsReportingViewMapper;
    this.paymentsReportingMapper = paymentsReportingMapper;
  }

  @Override
  public PagedPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);

    return paymentsReportingViewMapper.mapToPagedPaymentsReporting(
      paymentsReportingService.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken));
  }

  @Override
  public PagedPaymentsReportingRow getPaymentsReportingDetail(Long organizationId, String iuf, String iuv, LocalDateIntervalFilter payDateFilter, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);

    return paymentsReportingMapper.mapToPagedPaymentsReporting(
      paymentsReportingService.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, accessToken));
  }

}
