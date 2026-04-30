package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeServiceTest {

  @Mock
  private DebtPositionTypeClient client;

  private DebtPositionTypeService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeServiceImpl(client);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(client);
  }

  @Test
  void whenGetDebtPositionTypeByIdThenInvokeClient() {
    Long debtPositionTypeId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionType expectedResult = new DebtPositionType();

    when(client.getDebtPositionTypeById(Mockito.same(debtPositionTypeId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    DebtPositionType result = service.getDebtPositionTypeById(debtPositionTypeId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeWithCountByIdThenInvokeClient() {
    Long debtPositionTypeId = 1L;
    String code = "code";
    String description = "description";
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(client.getDebtPositionTypeWithCount(Mockito.same(debtPositionTypeId), Mockito.same(code), Mockito.same(description), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = service.getDebtPositionTypeWithCount(debtPositionTypeId, code, description, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenCreateDebtPositionTypeThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    when(client.createDebtPositionType(debtPositionTypeRequestBody, accessToken))
      .thenReturn(expectedResult);

    DebtPositionType result = service.createDebtPositionType(debtPositionTypeRequestBody, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenPatchDebtPositionTypeThenInvokeClient() {
    Long debtPositionTypeId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    when(client.patchDebtPositionType(debtPositionTypeId,debtPositionTypeRequestBody, accessToken))
      .thenReturn(expectedResult);

    DebtPositionType result = service.patchDebtPositionType(debtPositionTypeId,debtPositionTypeRequestBody, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenValidRequestWhenDeleteDebtPositionTypeThenClientCalled() {
    long debtPositionTypeId = 123L;
    String accessToken = "TOKEN";

    Mockito.doNothing().when(client).deleteDebtPositionType(debtPositionTypeId, accessToken);

    service.deleteDebtPositionType(debtPositionTypeId, accessToken);

    Mockito.verify(client).deleteDebtPositionType(debtPositionTypeId, accessToken);
  }

  @Test
  void whenGetDebtPositionTypesByBrokerIdAndOrgTypeThenInvokeClient() {
    Long brokerId = 123L;
    String orgType = "ORG_TYPE";
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionType expectedResult = new CollectionModelDebtPositionType();

    when(client.getDebtPositionTypesByBrokerIdAndOrgType(brokerId, orgType, accessToken))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionType result = service.getDebtPositionTypesByBrokerIdAndOrgType(brokerId, orgType, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindByDebtPositionTypeIdsThenInvokeClient() {
    Set<Long> debtPositionTypeIds = Set.of(1L);
    String accessToken = "ACCESSTOKEN";
    List<DebtPositionType> expectedResult = new ArrayList<>();

    when(client.findByDebtPositionTypeIds(debtPositionTypeIds, accessToken))
      .thenReturn(expectedResult);

    List<DebtPositionType> result = service.findByDebtPositionTypeIds(debtPositionTypeIds, accessToken);

    assertSame(expectedResult, result);
  }

}
