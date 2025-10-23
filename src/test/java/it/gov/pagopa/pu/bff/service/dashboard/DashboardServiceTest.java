package it.gov.pagopa.pu.bff.service.dashboard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.mapper.DashboardMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
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

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

  private DashboardService dashboardService;

  @Mock
  private InstallmentRetrieverService installmentRetrieverServiceMock;
  @Mock
  private ClassificationRetrieverService classificationRetrieverServiceMock;
  @Mock
  private DashboardMapper dashboardMapperMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setup() {
    dashboardService = new DashboardServiceImpl(
      installmentRetrieverServiceMock, classificationRetrieverServiceMock, dashboardMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      installmentRetrieverServiceMock,
      classificationRetrieverServiceMock,
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
  void whenGetDashboardByIuvThenOk() {
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

    ClassificationFiltersDTO expectedClassificationFilters = ClassificationFiltersDTO.builder()
      .iuv(iuv)
      .build();

    PagedInstallmentView installments = podamFactory.manufacturePojo(PagedInstallmentView.class);
    PagedModelClassification classifications = podamFactory.manufacturePojo(PagedModelClassification.class);

    DashboardByIuv expected = new DashboardByIuv();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedInstallmentFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(classificationRetrieverServiceMock.getClassifications(organizationId, expectedClassificationFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(classifications);

      when(dashboardMapperMock.mapToDashboardByIuv(installments, classifications))
        .thenReturn(expected);

      DashboardByIuv result = dashboardService.getDashboardByIuv(organizationId, iuv, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }

  @Test
  void whenGetDashboardByIufThenOk() {
    Long organizationId = 1L;
    String iuf = "iuf";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "TOKEN";

    ClassificationFiltersDTO expectedClassificationFilters = ClassificationFiltersDTO.builder()
      .iuf(iuf)
      .build();

    PagedModelClassification classifications = podamFactory.manufacturePojo(PagedModelClassification.class);

    DashboardByIuf expected = new DashboardByIuf();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationRetrieverServiceMock.getClassifications(organizationId, expectedClassificationFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(classifications);

      when(dashboardMapperMock.mapToDashboardByIuf(classifications))
        .thenReturn(expected);

      DashboardByIuf result = dashboardService.getDashboardByIuf(organizationId, iuf, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }
}
