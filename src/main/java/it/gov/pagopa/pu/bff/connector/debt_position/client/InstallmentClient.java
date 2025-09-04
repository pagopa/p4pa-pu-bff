package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
public class InstallmentClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public InstallmentClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public PagedModelInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken) {
    return debtPositionApisHolder.getInstallmentViewSearchControllerApi(accessToken)
      .crudInstallmentViewsFindInstallmentsByFilters(
        installmentViewFiltersDTO.getOrganizationId(),
        installmentViewFiltersDTO.getOperatorExternalUserId(),
        installmentViewFiltersDTO.getDueDate().getFrom(),
        installmentViewFiltersDTO.getDueDate().getTo(),
        installmentViewFiltersDTO.getIuv(),
        installmentViewFiltersDTO.getFiscalCode(),
        installmentViewFiltersDTO.getDebtPositionTypeOrgId(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public InstallmentDetailDTO getInstallmentDetail(Long installmentId, String operatorExternalUserId, String accessToken) {
    try {
      return debtPositionApisHolder.getInstallmentApi(accessToken)
        .getInstallmentDetail(installmentId, operatorExternalUserId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("InstallmentDetail with installmentId {} and operatorExternalUserId {} not found", installmentId, operatorExternalUserId);
      return null;
    }
  }

  public InstallmentNoPII getInstallmentFromTransferSemanticKey(Long organizationId, String iuv, String iur, String transferIndex, String operatorExternalUserId, List<DebtPositionOrigin> debtPositionOrigins, String accessToken) {
    try {
      return debtPositionApisHolder.getInstallmentNoPiiSearchControllerApi(
          accessToken)
        .crudInstallmentsFindAuthorizedByTransferSemanticKey(organizationId,
          iuv, iur, transferIndex, operatorExternalUserId, debtPositionOrigins);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Installment with this semantic key and operatorExternalUserId {} not found", operatorExternalUserId);
      return null;
    }
  }

}
