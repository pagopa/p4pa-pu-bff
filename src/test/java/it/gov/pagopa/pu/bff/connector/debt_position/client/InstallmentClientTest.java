package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.InstallmentApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.InstallmentNoPiiSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.InstallmentViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class InstallmentClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private InstallmentViewSearchControllerApi installmentViewSearchControllerApiMock;
  @Mock
  private InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApi;
  @Mock
  private InstallmentApi installmentApiMock;

  private InstallmentClient installmentClient;

  @BeforeEach
  void setUp() {
    installmentClient = new InstallmentClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      installmentViewSearchControllerApiMock
    );
  }

  @Test
  void whenGetInstallmentsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelInstallmentView expectedResult = new PagedModelInstallmentView();

    LocalDate dueDateFrom = LocalDate.now().minusDays(30);
    LocalDate dueDateTo = LocalDate.now();
    LocalDateIntervalFilter dueDateFilter = new LocalDateIntervalFilter(dueDateFrom, dueDateTo);

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO(
      1L, "operatorExternalUserId", dueDateFilter, "iuv", "fiscalCode", Collections.emptyList(), 2L);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getInstallmentViewSearchControllerApi(accessToken))
      .thenReturn(installmentViewSearchControllerApiMock);

    when(installmentViewSearchControllerApiMock.crudInstallmentViewsFindInstallmentsByFilters(
      filtersDTO.getOrganizationId(),
      filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getDueDate().getFrom(),
      filtersDTO.getDueDate().getTo(),
      filtersDTO.getIuv(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionOrigins(),
      filtersDTO.getDebtPositionTypeOrgId(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelInstallmentView result = installmentClient.getInstallments(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetInstallmentDetailThenInvokeWithAccessToken() {
    Long installmentId = 123L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    InstallmentDetailDTO expectedResult = new InstallmentDetailDTO();

    when(debtPositionApisHolderMock.getInstallmentApi(accessToken))
      .thenReturn(installmentApiMock);
    when(installmentApiMock.getInstallmentDetail(installmentId, operatorExternalUserId))
      .thenReturn(expectedResult);

    InstallmentDetailDTO result = installmentClient.getInstallmentDetail(installmentId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExceptionWhenGetInstallmentDetailThenReturnNull() {
    Long installmentId = 123L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getInstallmentApi(accessToken))
      .thenReturn(installmentApiMock);
    when(installmentApiMock.getInstallmentDetail(installmentId, operatorExternalUserId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    InstallmentDetailDTO result = installmentClient.getInstallmentDetail(installmentId, operatorExternalUserId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenGetInstallmentFromTransferSemanticKeyThenInvokeWithAccessToken() {
    Long organizationId = 1L;
    String iuv = "iuv";
    String iur = "iur";
    String transferIndex = "transferIndex";
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    InstallmentNoPII expectedResult = new InstallmentNoPII();

    when(debtPositionApisHolderMock.getInstallmentNoPiiSearchControllerApi(accessToken))
      .thenReturn(installmentNoPiiSearchControllerApi);
    when(installmentNoPiiSearchControllerApi.crudInstallmentsFindAuthorizedByTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, null))
      .thenReturn(expectedResult);

    InstallmentNoPII result = installmentClient.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, null, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExceptionWhenGetInstallmentFromTransferSemanticKeyThenReturnNull() {
    Long organizationId = 1L;
    String iuv = "iuv";
    String iur = "iur";
    String transferIndex = "transferIndex";
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getInstallmentNoPiiSearchControllerApi(accessToken))
      .thenReturn(installmentNoPiiSearchControllerApi);
    when(installmentNoPiiSearchControllerApi.crudInstallmentsFindAuthorizedByTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, null))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    InstallmentNoPII result = installmentClient.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, null, accessToken);

    Assertions.assertNull(result);
  }

}
