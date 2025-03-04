package it.gov.pagopa.pu.bff.service.installment;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import org.springframework.data.domain.Pageable;

public interface InstallmentRetrieverService {
  PagedInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  InstallmentDetailDTO getInstallmentDetail(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken);
}
