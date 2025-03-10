package it.gov.pagopa.pu.bff.service.payments_reporting;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingDTOMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingRetrieverServiceImpl implements PaymentsReportingRetrieverService {

  private final PaymentsReportingService paymentsReportingService;
  private final InstallmentRetrieverService installmentRetrieverService;
  private final ReceiptRetrieverService receiptRetrieverService;
  private final PaymentsReportingDTOMapper paymentsReportingDTOMapper;
  private final PaymentsReportingViewMapper paymentsReportingViewMapper;

  public PaymentsReportingRetrieverServiceImpl(
    PaymentsReportingService paymentsReportingService,
    InstallmentRetrieverService installmentRetrieverService,
    ReceiptRetrieverService receiptRetrieverService,
    PaymentsReportingDTOMapper paymentsReportingDTOMapper,
    PaymentsReportingViewMapper paymentsReportingViewMapper) {
    this.paymentsReportingService = paymentsReportingService;
    this.installmentRetrieverService = installmentRetrieverService;
    this.receiptRetrieverService = receiptRetrieverService;
    this.paymentsReportingDTOMapper = paymentsReportingDTOMapper;
    this.paymentsReportingViewMapper = paymentsReportingViewMapper;
  }

  @Override
  public PagedPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);

    return paymentsReportingViewMapper.mapToPagedPaymentsReporting(
      paymentsReportingService.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken));
  }

  @Override
  public PaymentsReportingDetailDTO getPaymentsReportingDetail(Long organizationId, String paymentsReportingId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);

    PaymentsReporting paymentsReporting = paymentsReportingService.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken);
    InstallmentNoPII installment = installmentRetrieverService.getInstallmentFromTransferSemanticKey(organizationId, paymentsReporting.getIuv(), paymentsReporting.getIur(),
      String.valueOf(paymentsReporting.getTransferIndex()), loggedUser, accessToken);
    ReceiptDetailDTO receiptDetailDTO = receiptRetrieverService.getReceiptDetail(organizationId, installment.getReceiptId(), loggedUser, accessToken);

    return paymentsReportingDTOMapper.mapToPaymentsReportingDetailDTO(paymentsReporting, receiptDetailDTO);
  }

}
