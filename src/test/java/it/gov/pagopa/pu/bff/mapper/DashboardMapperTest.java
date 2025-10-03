package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DashboardMapperTest {

  private final DashboardMapper mapper = new DashboardMapper();

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenValidInputWhenMapToDashboardByFcThenCorrectMapping() {
    PagedInstallmentView installments = podamFactory.manufacturePojo(
      PagedInstallmentView.class);

    DashboardByFc expected = DashboardByFc.builder()
      .hasInstallment(true)
      .hasDebtPosition(true)
      .hasReceipt(true)
      .build();

    DashboardByFc result = mapper.mapToDashboardByFc(
      installments);

    assertEquals(expected, result);
  }

  @Test
  void givenValidInputWithSingleInstallmentWhenMapToDashboardByFcThenCorrectMapping() {
    List<InstallmentView> content = List.of(
      podamFactory.manufacturePojo(InstallmentView.class));
    PagedInstallmentView installments = PagedInstallmentView.builder()
      .content(content)
      .size(1L)
      .totalPages(1L)
      .totalElements(1L)
      .number(0L)
      .build();

    DashboardByFc expected = DashboardByFc.builder()
      .hasInstallment(true)
      .installmentId(content.getLast().getInstallmentId())
      .hasDebtPosition(true)
      .debtPositionId(content.getFirst().getDebtPositionId())
      .hasReceipt(true)
      .receiptId(content.getFirst().getReceiptId())
      .build();

    DashboardByFc result = mapper.mapToDashboardByFc(
      installments);

    assertEquals(expected, result);
  }

  @Test
  void givenNullInputWhenMapToDashboardByFcThenEmptyDTO() {
    DashboardByFc expected = DashboardByFc.builder()
      .hasInstallment(false)
      .hasDebtPosition(false)
      .hasReceipt(false)
      .build();

    assertEquals(expected, mapper.mapToDashboardByFc(null));
  }
}
