package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.springframework.data.domain.Pageable;

public interface InstallmentService {
  PagedModelInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken);
  InstallmentDetailDTO getInstallmentDetail(Long installmentId, String operatorExternalUserId, String accessToken);
}
