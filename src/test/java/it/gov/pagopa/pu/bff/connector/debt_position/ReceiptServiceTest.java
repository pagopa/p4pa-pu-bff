package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
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
class ReceiptServiceTest {

  @Mock
  private ReceiptClient client;

  private ReceiptService service;

  @BeforeEach
  void setUp() {
    service = new ReceiptServiceImpl(client);
  }

  @Test
  void whenGetReceiptByIdThenInvokeClient() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelReceiptView expectedResult = new PagedModelReceiptView();

    when(client.getReceipts(Mockito.same(filtersDTO), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelReceiptView result = service.getReceipts(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

}
