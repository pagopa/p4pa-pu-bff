package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeWithCountSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import java.util.Collections;
import java.util.List;
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
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApiMock;
  @Mock
  private DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApiMock;

  private DebtPositionTypeClient debtPositionTypeClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeClient = new DebtPositionTypeClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      debtPositionTypeWithCountSearchControllerApiMock,
      debtPositionTypeEntityControllerApiMock
      );
  }

  @Test
  void whenGetDebtPositionTypeByIdThenReturnDebtPositionType() {
    Long debtPositionTypeId = 123L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionType expectedResult = new DebtPositionType();
    expectedResult.setDebtPositionTypeId(debtPositionTypeId);

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudGetDebtpositiontype(String.valueOf(debtPositionTypeId)))
      .thenReturn(expectedResult);

    DebtPositionType result = debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNonExistentDebtPositionTypeIdWhenGetDebtPositionTypeByIdThenReturnNull() {
    Long debtPositionTypeId = 123L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudGetDebtpositiontype(String.valueOf(debtPositionTypeId)))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    DebtPositionType result = debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenGetDebtPositionTypeWithCountThenInvokeWithAccessToken() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetDebtPositionTypeWithCountThenInvokeWithAccessToken() {
    long brokerId = 1L;
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,null, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }


  @Test
  void givenNoExistentBrokerIdWhenGetDebtPositionTypeWithCountThenNull() {
    long brokerId = 1L;
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId,0,10,sortList))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId,PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenCreateDebtPositionTypeThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudCreateDebtpositiontype(
      debtPositionTypeRequestBody))
      .thenReturn(expectedResult);

    DebtPositionType result = debtPositionTypeClient.createDebtPositionType(
      debtPositionTypeRequestBody, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenPatchDebtPositionTypeThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long debtPositionTypeId = 1L;
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudPatchDebtpositiontype(
      debtPositionTypeId.toString(),debtPositionTypeRequestBody))
      .thenReturn(expectedResult);

    DebtPositionType result = debtPositionTypeClient.patchDebtPositionType(
      debtPositionTypeId,debtPositionTypeRequestBody, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentDebtPositionTypeWhenPatchDebtPositionTypeThenNull() {
    String accessToken = "ACCESSTOKEN";
    Long debtPositionTypeId = 1L;
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudPatchDebtpositiontype(
      debtPositionTypeId.toString(),debtPositionTypeRequestBody))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    DebtPositionType result = debtPositionTypeClient.patchDebtPositionType(
      debtPositionTypeId,debtPositionTypeRequestBody,accessToken);

    Assertions.assertNull(result);
  }

}

