package it.gov.pagopa.pu.bff.service.installment;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallmentRetrieverServiceImpl implements InstallmentRetrieverService {

  private final InstallmentViewMapper installmentViewMapper;
  private final InstallmentService installmentService;
  private final List<InstallmentDetailDTO.StatusEnum> statusList = List.of(InstallmentDetailDTO.StatusEnum.PAID, InstallmentDetailDTO.StatusEnum.REPORTED);

  public InstallmentRetrieverServiceImpl(
    InstallmentViewMapper installmentViewMapper,
    InstallmentService installmentService) {
    this.installmentViewMapper = installmentViewMapper;
    this.installmentService = installmentService;
  }

  @Override
  public PagedInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(installmentViewFiltersDTO.getOrganizationId(), loggedUser);
    return installmentViewMapper.mapToPagedInstallmentView(installmentService.getInstallments(installmentViewFiltersDTO, pageable, accessToken));
  }

  @Override
  public InstallmentDetailDTO getInstallmentDetail(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);
    InstallmentDetailDTO installmentDetailDTO = installmentService.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    setPaymentInfo(installmentDetailDTO);
    return installmentDetailDTO;
  }

  @Override
  public InstallmentNoPII getInstallmentFromTransferSemanticKey(
    Long organizationId, String iuv, String iur, String transferIndex, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser);
    return installmentService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex,
      loggedUser.getMappedExternalUserId(), accessToken);
  }


  private void setPaymentInfo(InstallmentDetailDTO installmentDetailDTO) {
    if (!statusList.contains(installmentDetailDTO.getStatus())) {
      installmentDetailDTO.setPayer(null);
      installmentDetailDTO.setPaymentDateTime(null);
      installmentDetailDTO.setIud(null);
      installmentDetailDTO.setIur(null);
      installmentDetailDTO.setPspCompanyName(null);
    }
  }

}
