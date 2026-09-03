package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeWithCountSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
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
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApiMock;
  @Mock
  private DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApiMock;
  @Mock
  private DebtPositionTypeSearchControllerApi debtPositionTypeSearchControllerApiMock;

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
      debtPositionTypeEntityControllerApiMock,
      debtPositionTypeSearchControllerApiMock
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
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    DebtPositionType result = debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken);

    assertNull(result);
  }

  @Test
  void whenGetDebtPositionTypeWithCountThenInvokeWithAccessToken() {
    long brokerId = 1L;
    String code = "code";
    String description = "description";
    List<String> sortList = List.of("sort1,ASC", "sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId, code, description, 0, 10, sortList))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId, code, description, PageRequest.of(0, 10,
        Sort.by(List.of(Order.asc("sort1"), Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetDebtPositionTypeWithCountThenInvokeWithAccessToken() {
    long brokerId = 1L;
    String code = "code";
    String description = "description";
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId, code, description, 0, null, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId, code, description, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }


  @Test
  void givenNoExistentBrokerIdWhenGetDebtPositionTypeWithCountThenNull() {
    long brokerId = 1L;
    String code = "code";
    String description = "description";
    List<String> sortList = List.of("sort1,ASC", "sort2,DESC");
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeWithCountSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeWithCountSearchControllerApiMock);
    when(debtPositionTypeWithCountSearchControllerApiMock.crudDebtPositionTypesWithCountFindByBrokerId(
      brokerId, code, description, 0, 10, sortList))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    PagedModelDebtPositionTypeWithCount result = debtPositionTypeClient.getDebtPositionTypeWithCount(
      brokerId, code, description, PageRequest.of(0, 10,
        Sort.by(List.of(Order.asc("sort1"), Order.desc("sort2")))), accessToken);

    assertNull(result);
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
      debtPositionTypeId.toString(), debtPositionTypeRequestBody))
      .thenReturn(expectedResult);

    DebtPositionType result = debtPositionTypeClient.patchDebtPositionType(
      debtPositionTypeId, debtPositionTypeRequestBody, accessToken);

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
      debtPositionTypeId.toString(), debtPositionTypeRequestBody))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    DebtPositionType result = debtPositionTypeClient.patchDebtPositionType(
      debtPositionTypeId, debtPositionTypeRequestBody, accessToken);

    assertNull(result);
  }

  @Test
  void givenExistingDebtPositionTypeWhenDeleteDebtPositionTypeThenInvokeWithAccessToken() {
    Long debtPositionTypeId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    doNothing().when(debtPositionTypeEntityControllerApiMock).crudDeleteDebtpositiontype(String.valueOf(debtPositionTypeId));

    Assertions.assertDoesNotThrow(() -> debtPositionTypeClient.deleteDebtPositionType(debtPositionTypeId, accessToken));
  }

  @Test
  void givenNoDebtPositionTypeWhenDeleteDebtPositionTypeThenThrowResourceNotFoundException() {
    Long debtPositionTypeId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    doThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"))
      .when(debtPositionTypeEntityControllerApiMock).crudDeleteDebtpositiontype(String.valueOf(debtPositionTypeId));

    Assertions.assertThrows(NotFoundException.class, () ->
      debtPositionTypeClient.deleteDebtPositionType(debtPositionTypeId, accessToken));
  }

  @Test
  void whenGetDebtPositionTypesByBrokerIdAndOrgTypeThenReturnResult() {
    Long brokerId = 123L;
    String orgType = "ORG_TYPE";
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionType expectedResult = new CollectionModelDebtPositionType();

    when(debtPositionApisHolderMock.getDebtPositionTypeSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeSearchControllerApiMock);
    when(debtPositionTypeSearchControllerApiMock.crudDebtPositionTypesFindAllByBrokerIdAndOrgType(brokerId, orgType))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionType result = debtPositionTypeClient.getDebtPositionTypesByBrokerIdAndOrgType(brokerId, orgType, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindByDebtPositionTypeIdsThenReturnResult() {
    Set<Long> debtPositionTypes = Set.of(1L);
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionType expectedResult = podamFactory.manufacturePojo(CollectionModelDebtPositionType.class);

    when(debtPositionApisHolderMock.getDebtPositionTypeSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeSearchControllerApiMock);
    when(debtPositionTypeSearchControllerApiMock.crudDebtPositionTypesFindByDebtPositionTypeIdIn(debtPositionTypes))
      .thenReturn(expectedResult);

    List<DebtPositionType> result = debtPositionTypeClient.findByDebtPositionTypeIds(debtPositionTypes, accessToken);

    assertSame(expectedResult.getEmbedded().getDebtPositionTypes(), result);
  }

  @Test
  void givenEmbeddedNullWhenFindByDebtPositionTypeIdsThenReturnEmptyList() {
    Set<Long> debtPositionTypes = Set.of(1L);
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionType expectedResult = podamFactory.manufacturePojo(CollectionModelDebtPositionType.class);
    expectedResult.setEmbedded(null);

    when(debtPositionApisHolderMock.getDebtPositionTypeSearchControllerApi(accessToken))
            .thenReturn(debtPositionTypeSearchControllerApiMock);
    when(debtPositionTypeSearchControllerApiMock.crudDebtPositionTypesFindByDebtPositionTypeIdIn(debtPositionTypes))
            .thenReturn(expectedResult);

    List<DebtPositionType> result = debtPositionTypeClient.findByDebtPositionTypeIds(debtPositionTypes, accessToken);

    assertNotNull(result);
    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenCollectionModelNullWhenFindByDebtPositionTypeIdsThenReturnEmptyList() {
    Set<Long> debtPositionTypes = Set.of(1L);
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeSearchControllerApi(accessToken))
            .thenReturn(debtPositionTypeSearchControllerApiMock);
    when(debtPositionTypeSearchControllerApiMock.crudDebtPositionTypesFindByDebtPositionTypeIdIn(debtPositionTypes))
            .thenReturn(null);

    List<DebtPositionType> result = debtPositionTypeClient.findByDebtPositionTypeIds(debtPositionTypes, accessToken);

    assertNotNull(result);
    assertTrue(CollectionUtils.isEmpty(result));
  }

}

