package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgWithCountSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;
  @Mock
  private DebtPositionTypeOrgEntityControllerApi debtPositionTypeOrgEntityControllerApiMock;
  @Mock
  private DebtPositionTypeOrgWithCountSearchControllerApi debtPositionTypeOrgWithCountSearchControllerApiMock;

  private DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgClient = new DebtPositionTypeOrgClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgSearchControllerApiMock, debtPositionTypeOrgEntityControllerApiMock, debtPositionTypeOrgWithCountSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrg expectedResult = new CollectionModelDebtPositionTypeOrg();

    long organizationId = 1L;
    String operatorExternalUserId = "operator123";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(
      String.valueOf(organizationId), operatorExternalUserId))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExistingDebtPositionTypeOrgWhenGetDebtPositionTypeOrgThenInvokeWithAccessToken() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgEntityControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgEntityControllerApiMock);

    when(debtPositionTypeOrgEntityControllerApiMock.crudGetDebtpositiontypeorg(
      String.valueOf(debtPositionTypeOrgId)))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoDebtPositionTypeOrgWhenGetDebtPositionTypeOrgThenReturnNull() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgEntityControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgEntityControllerApiMock);
    when(debtPositionTypeOrgEntityControllerApiMock.crudGetDebtpositiontypeorg(
      String.valueOf(debtPositionTypeOrgId)))
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);

    assertNull( result);
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
      organizationId, code, description, PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrgWithCount result = debtPositionTypeOrgClient.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoDebtPositionTypeOrgWithCountWhenGetDebtPositionTypeOrgWithCountThenReturnNull() {
    Long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgWithCountSearchControllerApiMock);

    when(debtPositionTypeOrgWithCountSearchControllerApiMock.crudDebtPositionTypeOrgsWithCountFindByCodeAndDescription(
      organizationId, code, description, PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    PagedModelDebtPositionTypeOrgWithCount result = debtPositionTypeOrgClient.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);

    assertNull(result);
  }

}
