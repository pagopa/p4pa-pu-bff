package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.InstallmentClient;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
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
class InstallmentServiceImplTest {

  @Mock
  private InstallmentClient client;

  private InstallmentService service;

  @BeforeEach
  void setUp() {
    service = new InstallmentServiceImpl(client);
  }

  @Test
  void whenGetInstallmentsThenInvokeClient() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelInstallmentView expectedResult = new PagedModelInstallmentView();

    when(client.getInstallments(Mockito.same(filtersDTO), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelInstallmentView result = service.getInstallments(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
