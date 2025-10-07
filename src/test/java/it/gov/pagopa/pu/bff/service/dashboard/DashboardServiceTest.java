package it.gov.pagopa.pu.bff.service.dashboard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

  private DashboardService dashboardService;

  @Mock
  private InstallmentRetrieverService installmentRetrieverServiceMock;
  @Mock
  private ClassificationRetrieverService classificationRetrieverServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;
  @Mock
  private DashboardMapper dashboardMapperMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setup() {
    dashboardService = new DashboardServiceImpl(installmentRetrieverServiceMock, classificationRetrieverServiceMock, organizationServiceMock, dashboardMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      installmentRetrieverServiceMock,
      classificationRetrieverServiceMock,
      organizationServiceMock,
      dashboardMapperMock
    );
  }

  @Test
  void whenGetDashboardByFiscalCodeThenOk() {
    Long organizationId = 1L;
    String fiscalCode = "fiscalCode";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    InstallmentViewFiltersDTO expectedFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .fiscalCode(fiscalCode)
      .debtPositionOrigins(null)
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);

    DashboardByFc expected = new DashboardByFc();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(dashboardMapperMock.mapToDashboardByFc(installments))
        .thenReturn(expected);

      DashboardByFc result = dashboardService.getDashboardByFiscalCode(organizationId, fiscalCode, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }

  @Test
  void givenOrganizationWithBothFlagsTrueWhenGetDashboardByIuvThenOk() {
    Long organizationId = 1L;
    String iuv = "iuv";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(true);

    InstallmentViewFiltersDTO expectedInstallmentFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .iuv(iuv)
      .debtPositionOrigins(null)
      .build();

    ClassificationFiltersDTO expectedClassificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .labels(Arrays.asList(ClassificationsEnum.values()))
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);
    PagedModelClassification classifications = podamFactory.manufacturePojo(PagedModelClassification.class);

    DashboardByIuv expected = new DashboardByIuv();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedInstallmentFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
        .thenReturn(organization);

      when(classificationRetrieverServiceMock.getClassifications(organizationId, expectedClassificationFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(classifications);

      when(dashboardMapperMock.mapToDashboardByIuv(installments, classifications))
        .thenReturn(expected);

      DashboardByIuv result = dashboardService.getDashboardByIuv(organizationId, iuv, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }

  @Test
  void givenOrganizationWithFalsePaymentFlagTrueWhenGetDashboardByIuvThenOk() {
    Long organizationId = 1L;
    String iuv = "iuv";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(false);
    organization.setFlagTreasury(true);

    InstallmentViewFiltersDTO expectedInstallmentFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .iuv(iuv)
      .debtPositionOrigins(null)
      .build();

    List<ClassificationsEnum> labels = new ArrayList<>(Arrays.asList(ClassificationsEnum.values()));

    labels.removeAll(List.of(
      ClassificationsEnum.RT_NO_IUD,
      ClassificationsEnum.IUD_NO_RT
    ));

    ClassificationFiltersDTO expectedClassificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .labels(labels)
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);
    PagedModelClassification classifications = podamFactory.manufacturePojo(PagedModelClassification.class);

    DashboardByIuv expected = new DashboardByIuv();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedInstallmentFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
        .thenReturn(organization);

      when(classificationRetrieverServiceMock.getClassifications(organizationId, expectedClassificationFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(classifications);

      when(dashboardMapperMock.mapToDashboardByIuv(installments, classifications))
        .thenReturn(expected);

      DashboardByIuv result = dashboardService.getDashboardByIuv(organizationId, iuv, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }

  @Test
  void givenOrganizationWithFalseTreasuryFlagWhenGetDashboardByIuvThenOk() {
    Long organizationId = 1L;
    String iuv = "iuv";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(false);

    InstallmentViewFiltersDTO expectedInstallmentFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .iuv(iuv)
      .debtPositionOrigins(null)
      .build();

    List<ClassificationsEnum> labels = new ArrayList<>(Arrays.asList(ClassificationsEnum.values()));

    labels.removeAll(List.of(
      ClassificationsEnum.RT_TES,
      ClassificationsEnum.RT_IUF_TES,
      ClassificationsEnum.IUF_NO_TES,
      ClassificationsEnum.TES_NO_IUF_OR_IUV,
      ClassificationsEnum.IUF_TES_DIV_IMP,
      ClassificationsEnum.TES_NO_MATCH
    ));

    ClassificationFiltersDTO expectedClassificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .labels(labels)
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);
    PagedModelClassification classifications = podamFactory.manufacturePojo(PagedModelClassification.class);

    DashboardByIuv expected = new DashboardByIuv();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedInstallmentFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
        .thenReturn(organization);

      when(classificationRetrieverServiceMock.getClassifications(organizationId, expectedClassificationFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(classifications);

      when(dashboardMapperMock.mapToDashboardByIuv(installments, classifications))
        .thenReturn(expected);

      DashboardByIuv result = dashboardService.getDashboardByIuv(organizationId, iuv, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }

  @Test
  void givenNullOrganizationWhenGetDashboardByIuvThenThrowResourceNotFoundException() {
    Long organizationId = 1L;
    String iuv = "iuv";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    InstallmentViewFiltersDTO expectedInstallmentFilters = InstallmentViewFiltersDTO.builder()
      .organizationId(organizationId)
      .operatorExternalUserId(loggedUser.getMappedExternalUserId())
      .iuv(iuv)
      .debtPositionOrigins(null)
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);
    installments.setTotalElements(1L);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedInstallmentFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
        .thenReturn(null);

      ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
        dashboardService.getDashboardByIuv(organizationId, iuv, loggedUser, accessToken)
      );

      assertEquals("Organization having ID " + organizationId + " not found", exception.getMessage());
    }
  }
}
