package it.gov.pagopa.pu.bff.service.dashboard;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.DashboardMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
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
  private DashboardMapper dashboardMapperMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setup() {
    dashboardService = new DashboardServiceImpl(installmentRetrieverServiceMock, dashboardMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      installmentRetrieverServiceMock,
      dashboardMapperMock
    );
  }

  @Test
  void whenGetInstallmentsByFiscalCodeThenOk() {
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

    PagedDashboardDTO expected = new PagedDashboardDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(installmentRetrieverServiceMock.getInstallments(expectedFilters, Pageable.ofSize(10), loggedUser, accessToken))
        .thenReturn(installments);

      when(dashboardMapperMock.mapToPagedDashboardByFcDTO(installments))
        .thenReturn(expected);

      PagedDashboardDTO result = dashboardService.getInstallmentsByFiscalCode(organizationId, fiscalCode, loggedUser, accessToken);

      assertSame(expected, result);
    }
  }
}
