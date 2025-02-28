package it.gov.pagopa.pu.bff.service.installment;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstallmentRetrieverServiceImpl implements InstallmentRetrieverService {

  private final InstallmentViewMapper installmentViewMapper;
  private final InstallmentService installmentService;
  private final InstallmentDetailDTOMapper installmentDetailDTOMapper;

  public InstallmentRetrieverServiceImpl(
    InstallmentViewMapper installmentViewMapper,
    InstallmentService installmentService,
    InstallmentDetailDTOMapper installmentDetailDTOMapper) {
    this.installmentViewMapper = installmentViewMapper;
    this.installmentService = installmentService;
    this.installmentDetailDTOMapper = installmentDetailDTOMapper;
  }

  @Override
  public PagedInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(installmentViewFiltersDTO.getOrganizationId(), loggedUser);
    return installmentViewMapper.mapToPagedInstallmentView(installmentService.getInstallments(installmentViewFiltersDTO, pageable, accessToken));
  }

  @Override
  public InstallmentDetailDTO getInstallmentDetail(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);
    return installmentDetailDTOMapper.mapToInstallmentDetailDTO(installmentService.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken));
  }

}
