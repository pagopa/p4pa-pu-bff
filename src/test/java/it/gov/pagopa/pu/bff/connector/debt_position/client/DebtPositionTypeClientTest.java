package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.p4pa_debt_positions.controller.generated.DebtPositionTypeEntityControllerApi;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.DebtPositionType;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;

  @Mock
  private DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApiMock;

  private DebtPositionTypeClient debtPositionTypeClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeClient = new DebtPositionTypeClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
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
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    DebtPositionType result = debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenHttpExceptionWhenGetDebtPositionTypeByIdThenThrowIt() {
    Long debtPositionTypeId = 123L;
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudGetDebtpositiontype(String.valueOf(debtPositionTypeId)))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(),
      () -> debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetDebtPositionTypeByIdThenThrowIt() {
    Long debtPositionTypeId = 123L;
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(debtPositionApisHolderMock.getDebtPositionTypeControllerApi(accessToken))
      .thenReturn(debtPositionTypeEntityControllerApiMock);
    when(debtPositionTypeEntityControllerApiMock.crudGetDebtpositiontype(String.valueOf(debtPositionTypeId)))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(expectedException.getClass(),
      () -> debtPositionTypeClient.getDebtPositionTypeById(debtPositionTypeId, accessToken));

    Assertions.assertSame(expectedException, result);
  }

}

