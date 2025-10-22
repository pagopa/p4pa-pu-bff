package it.gov.pagopa.pu.bff.service.payments_reporting;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentsReportingRetrieverServiceImpl implements
  PaymentsReportingRetrieverService {

  private final PaymentsReportingService paymentsReportingService;
  private final InstallmentRetrieverService installmentRetrieverService;
  private final ReceiptRetrieverService receiptRetrieverService;
  private final PaymentsReportingViewMapper paymentsReportingViewMapper;
  private final PaymentsReportingMapper paymentsReportingMapper;

  public PaymentsReportingRetrieverServiceImpl(
    PaymentsReportingService paymentsReportingService,
    InstallmentRetrieverService installmentRetrieverService,
    ReceiptRetrieverService receiptRetrieverService,
    PaymentsReportingViewMapper paymentsReportingViewMapper,
    PaymentsReportingMapper paymentsReportingMapper) {
    this.paymentsReportingService = paymentsReportingService;
    this.installmentRetrieverService = installmentRetrieverService;
    this.receiptRetrieverService = receiptRetrieverService;
    this.paymentsReportingViewMapper = paymentsReportingViewMapper;
    this.paymentsReportingMapper = paymentsReportingMapper;
  }

  @Override
  public List<PaymentsReporting> getPaymentsReporting(Long organizationId, String iuf, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    return paymentsReportingService.getPaymentsReporting(organizationId, iuf, accessToken);
  };

  @Override
  public PagedPaymentsReportingView getPaymentsReportingView(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter, String iuv, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    validatePaymentsReportingFilters(iuf, regulationUniqueIdentifier, regulationDateFilter, iuv);

    return paymentsReportingViewMapper.mapToPagedPaymentsReporting(
      paymentsReportingService.getPaymentsReportingView(organizationId, iuf,
        regulationUniqueIdentifier, regulationDateFilter, iuv ,pageable,
        accessToken));
  }

  private void validatePaymentsReportingFilters(String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter, String iuv) {
    boolean hasPartialDateRange = regulationDateFilter != null &&
      (regulationDateFilter.getFrom() == null ^ regulationDateFilter.getTo() == null);
    if (hasPartialDateRange) {
      throw new IllegalArgumentException("Date interval is invalid: both 'from' and 'to' must be provided or both null");
    }
    if ((regulationDateFilter == null ||
      DateUtils.isNullOrInvalidLocalDateRange(regulationDateFilter.getFrom(), regulationDateFilter.getTo())) &&
      StringUtils.isBlank(iuf) &&
      StringUtils.isBlank(regulationUniqueIdentifier) &&
      StringUtils.isBlank(iuv)) {

      throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
    }
  }

  @Override
  public PagedPaymentsReportingRow getPaymentsReportingRows(Long organizationId,
                                                                           String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
                                                                           Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,
      loggedUser);

    return paymentsReportingMapper.mapToPagedPaymentsReporting(
      paymentsReportingService.getPaymentsReportingRows(organizationId, iuf,
        iuv, payDateFilter, pageable, accessToken));
  }

  @Override
  public PaymentsReportingDetailDTO getPaymentsReportingDetail(
    Long organizationId, String iuf, String paymentsReportingId, UserInfo loggedUser,
    String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,
      loggedUser);

    PaymentsReporting paymentsReporting = paymentsReportingService.getPaymentsReportingDetail(
      organizationId, paymentsReportingId, accessToken);

    if (paymentsReporting == null || !iuf.equals(paymentsReporting.getIuf())) {
      return null;
    }

    InstallmentNoPII installment = installmentRetrieverService.getInstallmentFromTransferSemanticKey(
      organizationId, paymentsReporting.getIuv(), paymentsReporting.getIur(),
      String.valueOf(paymentsReporting.getTransferIndex()), loggedUser, null,
      accessToken);
    ReceiptDetailDTO receiptDetailDTO = installment != null ?
      receiptRetrieverService.getReceiptDetail(organizationId,
        installment.getReceiptId(), loggedUser, accessToken)
      : null;

    return paymentsReportingMapper.mapToPaymentsReportingDetailDTO(
      paymentsReporting, receiptDetailDTO);
  }

}
