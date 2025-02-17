package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

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
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelDebtPositionTypeWithCount expectedResult = new PagedModelDebtPositionTypeWithCount();

    when(client.getDebtPositionTypeWithCount(Mockito.same(debtPositionTypeId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeWithCount result = service.getDebtPositionTypeWithCount(debtPositionTypeId, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
