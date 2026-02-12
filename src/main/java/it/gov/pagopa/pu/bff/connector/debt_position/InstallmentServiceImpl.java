package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.InstallmentClient;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedInstallmentsView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallmentServiceImpl implements InstallmentService {

  private final InstallmentClient client;

  public InstallmentServiceImpl(InstallmentClient client) {
    this.client = client;
  }

  @Override
  public PagedInstallmentsView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken) {
    return client.getInstallments(installmentViewFiltersDTO, pageable, accessToken);
  }

  @Override
  public InstallmentDetailDTO getInstallmentDetail(Long installmentId, String operatorExternalUserId, String accessToken) {
    return client.getInstallmentDetail(installmentId, operatorExternalUserId, accessToken);
  }

  @Override
  public InstallmentNoPII getInstallmentFromTransferSemanticKey(Long organizationId, String iuv, String iur, String transferIndex, String operatorExternalUserId, List<DebtPositionOrigin> debtPositionOrigins, String accessToken) {
    return client.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, debtPositionOrigins, accessToken);
  }
}
