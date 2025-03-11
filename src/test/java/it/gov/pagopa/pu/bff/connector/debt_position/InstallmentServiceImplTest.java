package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.InstallmentClient;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

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

  @Test
  void whenGetInstallmentDetailThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    Long installmentId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";
    InstallmentDetailDTO expectedResult = new InstallmentDetailDTO();

    when(client.getInstallmentDetail(installmentId, operatorExternalUserId, accessToken))
      .thenReturn(expectedResult);

    InstallmentDetailDTO result = service.getInstallmentDetail(installmentId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetInstallmentFromTransferSemanticKeyThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String iuv = "iuv";
    String iur = "iur";
    String transferIndex = "transferIndex";
    String operatorExternalUserId = "operatorExternalUserId";
    InstallmentNoPII expectedResult = new InstallmentNoPII();

    when(client.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, accessToken))
      .thenReturn(expectedResult);

    InstallmentNoPII result = service.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

}
