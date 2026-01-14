package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.ManageDebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;

import java.nio.charset.StandardCharsets;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApiMock;
  @Mock
  private DebtPositionApi debtPositionApiMock;
  @Mock
  private ObjectMapper objectMapperMock;

  private DebtPositionClient debtPositionClient;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    debtPositionClient = new DebtPositionClient(debtPositionApisHolderMock, objectMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            debtPositionApisHolderMock,
            debtPositionViewSearchControllerApiMock,
            debtPositionApiMock
    );
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
      operatorExternalUserId,
      List.of(DebtPositionOrigin.ORDINARY.toString(), DebtPositionOrigin.RECEIPT_FILE.toString()),
      filtersDTO.getCreationDateTimeFrom().toLocalDateTime(),
      filtersDTO.getCreationDateTimeTo().toLocalDateTime(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getStatus(),
      filtersDTO.getIuv(),
      filtersDTO.getIud(),
      1,
      10,
      Collections.emptyList()
    ))
      .thenReturn(expectedResult);

    PagedModelDebtPositionView result = debtPositionClient.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalUserId, PageRequest.of(1, 10), accessToken);

    Assertions.assertSame(expectedResult, result);
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

    Boolean deletedDebtPositionPhysically = debtPositionClient.deleteDebtPosition(debtPositionId, accessToken);

    Assertions.assertFalse(deletedDebtPositionPhysically);
  }

  @Test
  void givenExistingDebtPositionIdWhenDeleteDebtPositionThenInvokeWithAccessTokenAndReturnNoContent() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    ResponseEntity<Void> voidResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
      .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.deleteDebtPositionWithHttpInfo(debtPositionId))
      .thenReturn(voidResponseEntity);

    Boolean deletedDebtPositionPhysically = debtPositionClient.deleteDebtPosition(debtPositionId, accessToken);

    Assertions.assertTrue(deletedDebtPositionPhysically);
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
  }

  @Test
  void givenExistingDebtPositionWhenManageDebtPositionInstallmentsThenInvokeWithAccessToken() {
    Long debtPositionId = 1L;
    ManageDebtPositionDTO manageDebtPositionDTO = new ManageDebtPositionDTO();
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = new DebtPositionDTO();

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
            .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.manageDebtPositionInstallments(debtPositionId,manageDebtPositionDTO))
            .thenReturn(expectedResult);

    DebtPositionDTO result = debtPositionClient.manageDebtPositionInstallments(debtPositionId, manageDebtPositionDTO, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoDebtPositionWhenManageDebtPositionInstallmentsThenReturnNull() {
    Long debtPositionId = 1L;
    ManageDebtPositionDTO manageDebtPositionDTO = new ManageDebtPositionDTO();
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
            .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.manageDebtPositionInstallments(debtPositionId,manageDebtPositionDTO))
            .thenThrow(
                    HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> debtPositionClient.manageDebtPositionInstallments(debtPositionId, manageDebtPositionDTO, accessToken));

    Assertions.assertEquals("DebtPosition with ID 1 not found", ex.getMessage());
  }

  @Test
  void givenExistingDebtPositionWhenPublishDebtPositionThenInvokeWithAccessToken() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = new DebtPositionDTO();

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
            .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.publishDebtPosition(debtPositionId))
            .thenReturn(expectedResult);

    DebtPositionDTO result = debtPositionClient.publishDebtPosition(debtPositionId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoDebtPositionWhenPublishDebtPositionThenReturnNull() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
            .thenReturn(debtPositionApiMock);
    when(debtPositionApiMock.publishDebtPosition(debtPositionId))
            .thenThrow(
                    HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    Assertions.assertThrows(ResourceNotFoundException.class, () -> debtPositionClient.publishDebtPosition(debtPositionId, accessToken));
  }

  @Test
  void givenConflictWhenPublishDebtPositionThenReturnNull() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getDebtPositionApi(accessToken))
            .thenReturn(debtPositionApiMock);

    String body = """
    {"code":"UPSTREAM_CONFLICT","message":"[ALREADY_PUBLISHED] conflict","traceId":"t1"}
    """;

    HttpClientErrorException ex = HttpClientErrorException.create(
      HttpStatus.CONFLICT,
      "Conflict",
      HttpHeaders.EMPTY,
      body.getBytes(StandardCharsets.UTF_8),
      StandardCharsets.UTF_8
    );

    when(debtPositionApiMock.publishDebtPosition(debtPositionId))
            .thenThrow(ex);

    Assertions.assertThrows(ConflictException.class, () -> debtPositionClient.publishDebtPosition(debtPositionId, accessToken));
  }
}
