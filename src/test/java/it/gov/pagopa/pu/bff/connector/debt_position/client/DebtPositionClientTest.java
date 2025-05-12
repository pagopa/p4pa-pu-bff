package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApiMock;
  @Mock
  private DebtPositionApi debtPositionApiMock;

  private DebtPositionClient debtPositionClient;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    debtPositionClient = new DebtPositionClient(debtPositionApisHolderMock);
  }

  @Test
  void whenCreateDebtPositionThenInvokeWithAccessToken() {
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    Boolean massive = true;
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDTO.class);

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.createDebtPosition(debtPositionDTO, massive))
      .thenReturn(expectedResult);

    DebtPositionDTO result = debtPositionClient.createDebtPosition(debtPositionDTO, massive, accessToken);

    Assertions.assertSame(expectedResult, result);
    Mockito.verify(debtPositionApisHolderMock).getDebtPositionApi(accessToken);
    Mockito.verify(debtPositionApiMock).createDebtPosition(debtPositionDTO, massive);
  }

  @Test
  void whenGetDebtPositionViewsThenInvokeWithAccessToken() {
    DebtPositionViewFiltersDTO filtersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    List<String> debtPositionOrigins = List.of(DebtPositionOrigin.ORDINARY.toString(), DebtPositionOrigin.RECEIPT_FILE.toString());
    PagedModelDebtPositionView expectedResult = new PagedModelDebtPositionView();

    when(debtPositionApisHolderMock.getDebtPositionViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionViewSearchControllerApiMock);
    when(debtPositionViewSearchControllerApiMock.crudDebtPositionsViewFindDebtPositionViews(
      filtersDTO.getOrganizationId(),
      List.of(DebtPositionOrigin.ORDINARY.toString(), DebtPositionOrigin.RECEIPT_FILE.toString()),
      operatorExternalUserId,
      filtersDTO.getCreationDateFrom().toLocalDateTime(),
      filtersDTO.getCreationDateTo().toLocalDateTime(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getStatus(),
      1,
      10,
      Collections.emptyList()
    ))
      .thenReturn(expectedResult);

    PagedModelDebtPositionView result = debtPositionClient.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalUserId, PageRequest.of(1, 10), accessToken);

    Assertions.assertSame(expectedResult, result);
    Mockito.verify(debtPositionApisHolderMock).getDebtPositionViewSearchControllerApi(accessToken);
    Mockito.verify(debtPositionViewSearchControllerApiMock).crudDebtPositionsViewFindDebtPositionViews(
      filtersDTO.getOrganizationId(),
      List.of(DebtPositionOrigin.ORDINARY.getValue(), DebtPositionOrigin.RECEIPT_FILE.getValue()),
      operatorExternalUserId,
      filtersDTO.getCreationDateFrom().toLocalDateTime(),
      filtersDTO.getCreationDateTo().toLocalDateTime(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getStatus(),
      1,
      10,
      Collections.emptyList()
    );
  }

  @Test
  void givenExistingDebtPositionWhenGetDebtPositionThenInvokeWithAccessToken() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = new DebtPositionDTO();

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.getDebtPosition(debtPositionId))
      .thenReturn(expectedResult);

    DebtPositionDTO result = debtPositionClient.getDebtPosition(debtPositionId, accessToken);

    Assertions.assertSame(expectedResult, result);
    Mockito.verifyNoMoreInteractions(debtPositionApiMock, debtPositionApisHolderMock);
  }

  @Test
  void givenNoDebtPositionWhenGetDebtPositionThenReturnNull() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.getDebtPosition(debtPositionId))
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    DebtPositionDTO result = debtPositionClient.getDebtPosition(debtPositionId, accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoMoreInteractions(debtPositionApiMock, debtPositionApisHolderMock);
  }

  @Test
  void givenExistingDebtPositionIdWhenDeleteDebtPositionThenInvokeWithAccessToken() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    ResponseEntity<Void> voidResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.deleteDebtPositionWithHttpInfo(debtPositionId))
      .thenReturn(voidResponseEntity);

    ResponseEntity<Void> response = debtPositionClient.deleteDebtPosition(debtPositionId, accessToken);

    Assertions.assertSame(voidResponseEntity, response);
    Mockito.verifyNoMoreInteractions(debtPositionApiMock, debtPositionApisHolderMock);
  }

  @Test
  void givenWrongDebtPositionIdWhenDeleteDebtPositionThenThrowNotFoundException() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.deleteDebtPositionWithHttpInfo(debtPositionId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> debtPositionClient.deleteDebtPosition(debtPositionId, accessToken));

    Assertions.assertEquals("DebtPosition with ID 1 not found", ex.getMessage());
    Mockito.verifyNoMoreInteractions(debtPositionApiMock, debtPositionApisHolderMock);
  }

}
