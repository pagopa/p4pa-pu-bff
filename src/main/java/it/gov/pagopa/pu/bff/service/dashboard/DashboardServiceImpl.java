package it.gov.pagopa.pu.bff.service.dashboard;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByIuv;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DashboardMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final InstallmentRetrieverService installmentRetrieverService;
  private final ClassificationRetrieverService classificationRetrieverService;
  private final OrganizationService organizationService;

  private final DashboardMapper dashboardMapper;

  private static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(10);

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
      filters, DEFAULT_PAGEABLE, loggedUser, accessToken);

    return dashboardMapper.mapToDashboardByFc(installments);
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
      installmentFilters, Pageable.ofSize(10), loggedUser, accessToken);

    if (installments == null || installments.getTotalElements() == 0) {
      return dashboardMapper.mapToDashboardByIuv(installments, null);
    }

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);

    if(organization == null) {
      throw new ResourceNotFoundException("Organization having ID " + organizationId + " not found");
    }

    ClassificationFiltersDTO classificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .labels(getLabels(organization))
      .build();

    PagedModelClassification classifications = classificationRetrieverService.getClassifications(
      organizationId, classificationFilters, DEFAULT_PAGEABLE, loggedUser, accessToken);

    return dashboardMapper.mapToDashboardByIuv(installments, classifications);
  }

  private static List<ClassificationsEnum> getLabels(Organization organization) {
    List<ClassificationsEnum> labels = new ArrayList<>(Arrays.asList(ClassificationsEnum.values()));

    if (Boolean.FALSE.equals(organization.getFlagPaymentNotification())) {
      labels.removeAll(List.of(
        ClassificationsEnum.RT_NO_IUD,
        ClassificationsEnum.IUD_NO_RT
      ));
    }

    if (Boolean.FALSE.equals(organization.getFlagTreasury())) {
      labels.removeAll(List.of(
        ClassificationsEnum.RT_TES,
        ClassificationsEnum.RT_IUF_TES,
        ClassificationsEnum.IUF_NO_TES,
        ClassificationsEnum.TES_NO_IUF_OR_IUV,
        ClassificationsEnum.IUF_TES_DIV_IMP,
        ClassificationsEnum.TES_NO_MATCH
      ));
    }

    return labels;
  }
}
