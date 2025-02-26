package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.DebtPositionOriginEnum;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApiMock;

  private DebtPositionClient debtPositionClient;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    debtPositionClient = new DebtPositionClient(debtPositionApisHolderMock);
  }

  @Test
  void whenGetDebtPositionViewsThenInvokeWithAccessToken() {
    DebtPositionViewFiltersDTO filtersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    List<String> debtPositionOrigins = List.of(DebtPositionOriginEnum.ORDINARY.toString(),DebtPositionOriginEnum.RECEIPT_FILE.toString());
    PagedModelDebtPositionView expectedResult = new PagedModelDebtPositionView();

    when(debtPositionApisHolderMock.getDebtPositionViewSearchControllerApi(accessToken))
      .thenReturn(debtPositionViewSearchControllerApiMock);
    when(debtPositionViewSearchControllerApiMock.crudDebtPositionsViewFindDebtPositionViews(
      filtersDTO.getOrganizationId(),
      List.of(DebtPositionOriginEnum.ORDINARY.toString(),DebtPositionOriginEnum.RECEIPT_FILE.toString()),
      operatorExternalUserId,
      filtersDTO.getCreationDateFrom().toLocalDateTime(),
      filtersDTO.getCreationDateTo().toLocalDateTime(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getStatus().toString(),
      1,
      10,
      Collections.emptyList()
       ))
      .thenReturn(expectedResult);

    PagedModelDebtPositionView result = debtPositionClient.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalUserId,PageRequest.of(1,10),accessToken);

    Assertions.assertSame(expectedResult, result);
    Mockito.verify(debtPositionApisHolderMock).getDebtPositionViewSearchControllerApi(accessToken);
    Mockito.verify(debtPositionViewSearchControllerApiMock).crudDebtPositionsViewFindDebtPositionViews(
      filtersDTO.getOrganizationId(),
      List.of(DebtPositionOriginEnum.ORDINARY.toString(),DebtPositionOriginEnum.RECEIPT_FILE.toString()),
      operatorExternalUserId,
      filtersDTO.getCreationDateFrom().toLocalDateTime(),
      filtersDTO.getCreationDateTo().toLocalDateTime(),
      filtersDTO.getFiscalCode(),
      filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getStatus().toString(),
      1,
      10,
      Collections.emptyList()
    );
  }
}
