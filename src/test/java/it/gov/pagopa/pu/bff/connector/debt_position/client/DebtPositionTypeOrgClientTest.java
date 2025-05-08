package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
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
class DebtPositionTypeOrgClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;
  @Mock
  private DebtPositionTypeOrgEntityControllerApi debtPositionTypeOrgEntityControllerApiMock;
  @Mock
  private DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi debtPositionTypeOrgCountByOrganizationIdSearchControllerApiMock;
  @Mock
  private DebtPositionTypeOrgApi debtPositionTypeOrgApiMock;
  private DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgClient = new DebtPositionTypeOrgClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgSearchControllerApiMock,
      debtPositionTypeOrgEntityControllerApiMock, debtPositionTypeOrgCountByOrganizationIdSearchControllerApiMock, debtPositionTypeOrgApiMock);
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

    assertNull(result);
  }

  @Test
  void givenDebtPositionTypeOrgCountByOrganizationIdWhenGetDebtPositionTypeOrgCountByOrganizationIdThenInvokeWithAccessToken() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrgCountByOrganizationId expectedResult = new CollectionModelDebtPositionTypeOrgCountByOrganizationId();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgCountByOrganizationIdSearchControllerApiMock);

    when(debtPositionTypeOrgCountByOrganizationIdSearchControllerApiMock.crudDebtPositionTypeOrgsByOrganizationCountByOrganizationIds(
      List.of(debtPositionTypeOrgId)))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgCountByOrganizationId result = debtPositionTypeOrgClient.getDebtPositionTypeOrgCountByOrganizationId(List.of(debtPositionTypeOrgId), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExistingDebtPositionTypeOrgWhenDeleteDebtPositionTypeOrgThenInvokeWithAccessToken() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgApi(accessToken))
      .thenReturn(debtPositionTypeOrgApiMock);
    doNothing().when(debtPositionTypeOrgApiMock).deleteDebtPositionTypeOrg(
      debtPositionTypeOrgId);

    Assertions.assertDoesNotThrow(()->debtPositionTypeOrgClient.deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken));
  }

  @Test
  void givenNoDebtPositionTypeOrgWhenDeleteDebtPositionTypeOrgThenThrowResourceNotFoundException() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgApi(accessToken))
      .thenReturn(debtPositionTypeOrgApiMock);
    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
      .when(debtPositionTypeOrgApiMock).deleteDebtPositionTypeOrg(debtPositionTypeOrgId);

    Assertions.assertThrows(ResourceNotFoundException.class,()->
      debtPositionTypeOrgClient.deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken));
  }

  @Test
  void whenSaveDebtPositionTypeOrgThenInvokeWithAccessToken() {
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = TestUtils.getPodamFactory().manufacturePojo(SaveDebtPositionTypeOrgDTO.class);
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgApi(accessToken))
      .thenReturn(debtPositionTypeOrgApiMock);

    when(debtPositionTypeOrgApiMock.saveDebtPositionTypeOrg(
      saveDebtPositionTypeOrgDTO))
      .thenReturn(expectedResult);

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.saveDebtPositionTypeOrg(saveDebtPositionTypeOrgDTO, accessToken);

    assertSame(expectedResult, result);
  }
}
