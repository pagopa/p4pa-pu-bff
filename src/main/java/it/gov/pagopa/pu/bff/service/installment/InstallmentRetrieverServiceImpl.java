package it.gov.pagopa.pu.bff.service.installment;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstallmentRetrieverServiceImpl implements InstallmentRetrieverService {

  private final InstallmentViewMapper installmentViewMapper;
  private final InstallmentService installmentService;

  public InstallmentRetrieverServiceImpl(InstallmentViewMapper installmentViewMapper, InstallmentService installmentService) {
    this.installmentViewMapper = installmentViewMapper;
    this.installmentService = installmentService;
  }

  @Override
  public PagedInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(installmentViewFiltersDTO.getOrganizationId(), loggedUser);
    return installmentViewMapper.mapToPagedInstallmentView(installmentService.getInstallments(installmentViewFiltersDTO, pageable, accessToken));
  }

}
