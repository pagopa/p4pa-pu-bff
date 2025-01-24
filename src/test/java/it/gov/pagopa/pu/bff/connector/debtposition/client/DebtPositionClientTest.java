package it.gov.pagopa.pu.bff.connector.debtposition.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.p4pa_debt_positions.controller.generated.DebtPositionTypeWithCountSearchControllerApi;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCount;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class DebtPositionClientTest {
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApiMock;

  private DebtPositionClient debtPositionClient;

  @BeforeEach
  void setUp() {
    debtPositionClient = new DebtPositionClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock
    );
  }

  @Test
  void whenGetDebtPositionTypeWithCountThenInvokeWithAccessToken() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1","sort2");
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = debtPositionClient.getDebtPositionTypeWithCount(
      brokerId,0,10,
      sortList, accessToken);

    assertSame(expectedResult, result);
  }


  @Test
  void givenNoExistentBrokerIdWhenGetDebtPositionTypeWithCountThenNull() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1","sort2");
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    PagedModelDebtPositionTypeWithCount result = debtPositionClient.getDebtPositionTypeWithCount(
      brokerId,0,10,
      sortList, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetDebtPositionTypeWithCountThenThrowIt() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1","sort2");
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> debtPositionClient.getDebtPositionTypeWithCount(
      brokerId,0,10,
      sortList, accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetDebtPositionTypeWithCountThenThrowIt() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1","sort2");
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> debtPositionClient.getDebtPositionTypeWithCount(
        brokerId,0,10,
        sortList, accessToken));

    Assertions.assertSame(expectedException, result);
  }
}
