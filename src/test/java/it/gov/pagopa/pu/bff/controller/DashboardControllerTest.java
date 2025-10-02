package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.dashboard.DashboardService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

  @Mock
  private DashboardService dashboardServiceMock;

  @InjectMocks
  private DashboardController dashboardController;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(
    UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      dashboardServiceMock
    );
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetInstallmentsDashboardByFiscalCodeThenOk() {
    Long organizationId = 1L;
    String fiscalCode = "FRTMRA90C41F205D";

    PagedDashboardDTO expected = podamFactory.manufacturePojo(
      PagedDashboardDTO.class);

    Mockito.when(
        dashboardServiceMock.getInstallmentsByFiscalCode(organizationId,
          fiscalCode, loggedUser, accessToken))
      .thenReturn(expected);

    ResponseEntity<PagedDashboardDTO> response = dashboardController.getInstallmentsDashboardByFiscalCode(
      organizationId, fiscalCode);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expected, response.getBody());
  }


  @Test
  void givenNotFoundInstallmentsWhenGetInstallmentsDashboardByFiscalCodeThenNotFound() {
    Long organizationId = 1L;
    String fiscalCode = "FRTMRA90C41F205D";

    Mockito.when(
        dashboardServiceMock.getInstallmentsByFiscalCode(organizationId,
          fiscalCode, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<PagedDashboardDTO> response = dashboardController.getInstallmentsDashboardByFiscalCode(
      organizationId, fiscalCode);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}
