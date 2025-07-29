package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgWithCountSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgWithCountClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgWithCountSearchControllerApi debtPositionTypeOrgWithCountSearchControllerApiMock;

  private DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgWithCountClient = new DebtPositionTypeOrgWithCountClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgWithCountSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgWithCountThenInvokeWithAccessToken() {
    Long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeOrgWithCount expectedResult = new PagedModelDebtPositionTypeOrgWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgWithCountSearchControllerApiMock);

    when(debtPositionTypeOrgWithCountSearchControllerApiMock.crudDebtPositionTypeOrgsWithCountFindByCodeAndDescription(
      organizationId, code, description, true, PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrgWithCount result = debtPositionTypeOrgWithCountClient.getDebtPositionTypeOrgWithCount(organizationId, code, description, true, pageable, accessToken);

    assertSame(expectedResult, result);
  }

}
