package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentViewDTO;
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
    List<InstallmentViewDTO> content = List.of(
      podamFactory.manufacturePojo(InstallmentViewDTO.class));
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

  @Test
  void givenValidInputWhenMapToDashboardByIuvThenCorrectMapping() {
    PagedInstallmentView installments = podamFactory.manufacturePojo(
      PagedInstallmentView.class);

    PagedModelClassification classifications = podamFactory.manufacturePojo(
      PagedModelClassification.class);

    DashboardByIuv expected = DashboardByIuv.builder()
      .hasInstallment(true)
      .hasDebtPosition(true)
      .hasReceipt(true)
      .hasIuf(true)
      .hasClassification(true)
      .build();

    DashboardByIuv result = mapper.mapToDashboardByIuv(
      installments, classifications);

    assertEquals(expected, result);
  }

  @Test
  void givenNullPagedInstallmentsAndNullPagedClassificationsWhenMapToDashboardByIuvThenCorrectMapping() {
    DashboardByIuv expected = DashboardByIuv.builder()
      .hasInstallment(false)
      .hasDebtPosition(false)
      .hasReceipt(false)
      .hasIuf(false)
      .hasClassification(false)
      .build();

    assertEquals(expected, mapper.mapToDashboardByIuv(null,null));
  }

  @Test
  void givenNullPagedClassificationsWhenMapToDashboardByIuvThenCorrectMapping() {
    PagedInstallmentView installments = podamFactory.manufacturePojo(
      PagedInstallmentView.class);

    DashboardByIuv expected = DashboardByIuv.builder()
      .hasInstallment(true)
      .hasDebtPosition(true)
      .hasReceipt(true)
      .hasIuf(false)
      .hasClassification(false)
      .build();

    assertEquals(expected, mapper.mapToDashboardByIuv(installments,null));
  }

  @Test
  void givenValidInputWhenMapToDashboardByIufThenCorrectMapping() {
    PagedModelClassification classifications = podamFactory.manufacturePojo(
      PagedModelClassification.class);

    DashboardByIuf expected = DashboardByIuf.builder()
      .hasIuf(true)
      .hasClassification(true)
      .hasTreasury(true)
      .build();

    DashboardByIuf result = mapper.mapToDashboardByIuf(classifications);

    assertEquals(expected, result);
  }

  @Test
  void givenNullPagedClassificationsAndNullPagedTreasuriesWhenMapToDashboardByIufThenCorrectMapping() {
    DashboardByIuf expected = DashboardByIuf.builder()
      .hasIuf(false)
      .hasClassification(false)
      .hasTreasury(false)
      .build();

    assertEquals(expected, mapper.mapToDashboardByIuf(null));
  }

}
