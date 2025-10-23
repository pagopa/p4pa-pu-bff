package it.gov.pagopa.pu.bff.service.dashboard;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByIuf;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByIuv;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.mapper.DashboardMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final InstallmentRetrieverService installmentRetrieverService;
  private final ClassificationRetrieverService classificationRetrieverService;
  private final TreasuryRetrieverService treasuryRetrieverService;

  private final DashboardMapper dashboardMapper;

  private static final Pageable PAGE_CONFIG = Pageable.ofSize(10);

  @Override
  public DashboardByFc getDashboardByFiscalCode(Long organizationId,
    String fiscalCode, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    InstallmentViewFiltersDTO filters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .fiscalCode(fiscalCode)
      .debtPositionOrigins(null)
      .build();

    PagedInstallmentView installments = installmentRetrieverService.getInstallments(
      filters, PAGE_CONFIG, loggedUser, accessToken);

    return dashboardMapper.mapToDashboardByFc(installments);
  }

  @Override
  public DashboardByIuf getDashboardByIuf(Long organizationId, String iuf, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    ClassificationFiltersDTO classificationFilters = ClassificationFiltersDTO.builder()
      .iuf(iuf)
      .build();

    PagedModelClassification classifications = classificationRetrieverService.getClassifications(
      organizationId, classificationFilters, PAGE_CONFIG, loggedUser, accessToken);

    TreasuryViewFiltersDTO treasuryViewFiltersDTO = TreasuryViewFiltersDTO.builder()
      .organizationId(organizationId)
      .iuf(iuf)
      .build();

    PagedTreasuryView treasuries = treasuryRetrieverService.getTreasuries(treasuryViewFiltersDTO, PAGE_CONFIG, loggedUser, accessToken);

    return dashboardMapper.mapToDashboardByIuf(classifications, treasuries);
  }

  @Override
  public DashboardByIuv getDashboardByIuv(Long organizationId,
    String iuv, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    InstallmentViewFiltersDTO installmentFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .iuv(iuv)
      .debtPositionOrigins(null)
      .build();

    PagedInstallmentView installments = installmentRetrieverService.getInstallments(
      installmentFilters, PAGE_CONFIG, loggedUser, accessToken);

    if (installments == null || installments.getTotalElements() == 0) {
      return dashboardMapper.mapToDashboardByIuv(installments, null);
    }

    ClassificationFiltersDTO classificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .build();

    PagedModelClassification classifications = classificationRetrieverService.getClassifications(
      organizationId, classificationFilters, PAGE_CONFIG, loggedUser, accessToken);

    return dashboardMapper.mapToDashboardByIuv(installments, classifications);
  }
}
