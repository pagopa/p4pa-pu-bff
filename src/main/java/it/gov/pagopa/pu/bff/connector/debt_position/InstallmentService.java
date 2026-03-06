package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedInstallmentsView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstallmentService {
  PagedInstallmentsView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken);
  InstallmentDetailDTO getInstallmentDetail(Long installmentId, String operatorExternalUserId, String accessToken);
  InstallmentNoPII getInstallmentFromTransferSemanticKey(Long organizationId, String iuv, String iur, Integer transferIndex, String operatorExternalUserId, List<DebtPositionOrigin> debtPositionOrigins, String accessToken);
}
