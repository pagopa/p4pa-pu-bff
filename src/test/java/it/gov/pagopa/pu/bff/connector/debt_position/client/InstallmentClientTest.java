package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.InstallmentViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private InstallmentViewSearchControllerApi installmentViewSearchControllerApiMock;

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

    OffsetDateTime dueDateFrom = OffsetDateTime.now().minusDays(30);
    OffsetDateTime dueDateTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter dueDateFilter = new OffsetDateTimeIntervalFilter(dueDateFrom, dueDateTo);

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO(
      1L, "operatorExternalUserId", dueDateFilter, "iuv", "fiscalCode", 2L);
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
      filtersDTO.getDebtPositionTypeOrgId(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelInstallmentView result = installmentClient.getInstallments(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

}
