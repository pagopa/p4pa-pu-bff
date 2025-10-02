package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DashboardMapperTest {

  private final DashboardMapper mapper = new DashboardMapper();

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenValidInputWhenMapToPagedDashboardByFcThenCorrectMapping() {
    PagedInstallmentView installments = podamFactory.manufacturePojo(
      PagedInstallmentView.class);

    PagedDashboardDTO result = mapper.mapToPagedDashboardByFcDTO(installments);

    assertEquals(installments.getContent().size(), result.getContent().size());
    assertEquals(installments.getSize(), result.getSize());
    assertEquals(installments.getTotalPages(), result.getTotalPages());
    assertEquals(installments.getTotalElements(), result.getTotalElements());
    assertEquals(installments.getNumber(), result.getNumber());
  }

  @Test
  void givenNullInputWhenMapToPagedDashboardByFcDTOThenNull() {
    assertNull(mapper.mapToPagedDashboardByFcDTO(null));
  }

  @Test
  void givenValidInputWhenMapToDashboardByFcThenCorrectMapping() {
    InstallmentView installmentWithReceipt = podamFactory.manufacturePojo(
      InstallmentView.class);
    InstallmentView installmentNoReceipt = podamFactory.manufacturePojo(
      InstallmentView.class);
    installmentNoReceipt.setReceiptId(null);

    DashboardByFc resultWithReceipt = mapper.mapToDashboardByFc(
      installmentWithReceipt);

    assertTrue(resultWithReceipt.getHasInstallment());
    assertEquals(installmentWithReceipt.getInstallmentId(),
      resultWithReceipt.getInstallmentId());
    assertTrue(resultWithReceipt.getHasDebtPosition());
    assertEquals(installmentWithReceipt.getDebtPositionId(),
      resultWithReceipt.getDebtPositionId());
    assertTrue(resultWithReceipt.getHasReceipt());
    assertEquals(installmentWithReceipt.getReceiptId(),
      resultWithReceipt.getReceiptId());

    DashboardByFc resultNoReceipt = mapper.mapToDashboardByFc(
      installmentNoReceipt);

    assertTrue(resultNoReceipt.getHasInstallment());
    assertEquals(installmentNoReceipt.getInstallmentId(),
      resultNoReceipt.getInstallmentId());
    assertTrue(resultNoReceipt.getHasDebtPosition());
    assertEquals(installmentNoReceipt.getDebtPositionId(),
      resultNoReceipt.getDebtPositionId());
    assertFalse(resultNoReceipt.getHasReceipt());
    assertNull(resultNoReceipt.getReceiptId());
  }

  @Test
  void givenNullInputWhenMapToDashboardByFcThenEmptyDTO() {
    assertEquals(new DashboardByFc(), mapper.mapToDashboardByFc(null));
  }
}
