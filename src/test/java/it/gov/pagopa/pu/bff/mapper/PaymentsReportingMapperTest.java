package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonEntityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingMapperTest {

  private final PaymentsReportingMapper mapper = new PaymentsReportingMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPaymentsReportingThenCorrectMapping() {
    PagedModelPaymentsReportingWithReceiptView pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReportingWithReceiptView.class);

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(
      pagedModelPaymentsReporting);

    assertNotNull(result);
    assertEquals(pagedModelPaymentsReporting.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelPaymentsReporting.getPage().getSize(),
      result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelPaymentsReporting.getEmbedded().getPaymentsReportingWithReceiptViews(),
      result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelPaymentsReportingWithReceiptView pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReportingWithReceiptView.class);
    pagedModelPaymentsReporting.getEmbedded()
      .setPaymentsReportingWithReceiptViews(Collections.emptyList());

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(
      pagedModelPaymentsReporting);

    assertNotNull(result);
    assertEquals(pagedModelPaymentsReporting.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelPaymentsReporting.getPage().getSize(),
      result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelPaymentsReportingWithReceiptView pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReportingWithReceiptView.class);
    pagedModelPaymentsReporting.setPage(null);

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(
      pagedModelPaymentsReporting);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelPaymentsReporting.getEmbedded().getPaymentsReportingWithReceiptViews(),
      result.getContent());
  }

  @Test
  void givenPaymentsReportingAndReceiptDTOThenCorrectMapping() {
    PaymentsReporting paymentsReporting = PaymentsReporting.builder()
      .paymentsReportingId("PAYREP123")
      .organizationId(1L)
      .iuv("IUV123")
      .iur("IUR123")
      .transferIndex(1)
      .ingestionFlowFileId(1L)
      .pspIdentifier("PSPID")
      .iuf("IUF123")
      .flowDateTime(OffsetDateTime.now())
      .regulationDate(LocalDate.now())
      .regulationUniqueIdentifier("REG123")
      .senderPspCode("SENDERCODE")
      .senderPspName("SENDERNAME")
      .senderPspType("SENDERTYPE")
      .receiverOrganizationCode("RECEIVERCODE")
      .receiverOrganizationName("RECEIVERNAME")
      .receiverOrganizationType("RECEIVERTYPE")
      .totalPayments(1000L)
      .totalAmountCents(1000L)
      .amountPaidCents(1000L)
      .paymentOutcomeCode("OUTCOMECODE")
      .payDate(LocalDate.now())
      .acquiringDate(LocalDate.now())
      .revision(1)
      .build();
    ReceiptDetailDTO receiptDetailDTO = ReceiptDetailDTO.builder()
      .debtor(
        PersonDTO.builder().fiscalCode("ABCDEF00B00F205A").fullName("DEBTOR")
          .entityType(PersonEntityType.F).build())
      .paymentDateTime(OffsetDateTime.now())
      .iud("IUD123")
      .debtPositionTypeOrgDescription("DESCRIPTION")
      .pspCompanyName("COMPANY")
      .remittanceInformation("REMITTANCEINFO")
      .paymentAmountCents(1000L)
      .build();

    PaymentsReportingDetailDTO expected = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .debtor(receiptDetailDTO.getDebtor())
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .iud(receiptDetailDTO.getIud())
      .debtPositionTypeOrgDescription(
        receiptDetailDTO.getDebtPositionTypeOrgDescription())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .status(InstallmentStatus.REPORTED)
      .build();

    PaymentsReportingDetailDTO result = mapper.mapToPaymentsReportingDetailDTO(
      paymentsReporting, receiptDetailDTO);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    assertEquals(expected, result);
  }

  @Test
  void givenPaymentsReportingAndNullReceiptDTOThenPartialMapping() {
    PaymentsReporting paymentsReporting = PaymentsReporting.builder()
      .paymentsReportingId("PAYREP123")
      .organizationId(1L)
      .iuv("IUV123")
      .iur("IUR123")
      .transferIndex(1)
      .ingestionFlowFileId(1L)
      .pspIdentifier("PSPID")
      .iuf("IUF123")
      .flowDateTime(OffsetDateTime.now())
      .regulationDate(LocalDate.now())
      .regulationUniqueIdentifier("REG123")
      .senderPspCode("SENDERCODE")
      .senderPspName("SENDERNAME")
      .senderPspType("SENDERTYPE")
      .receiverOrganizationCode("RECEIVERCODE")
      .receiverOrganizationName("RECEIVERNAME")
      .receiverOrganizationType("RECEIVERTYPE")
      .totalPayments(1000L)
      .totalAmountCents(1000L)
      .amountPaidCents(1000L)
      .paymentOutcomeCode("OUTCOMECODE")
      .payDate(LocalDate.now())
      .acquiringDate(LocalDate.now())
      .revision(1)
      .build();

    PaymentsReportingDetailDTO expected = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(InstallmentStatus.REPORTED)
      .build();

    PaymentsReportingDetailDTO result = mapper.mapToPaymentsReportingDetailDTO(
      paymentsReporting, null);

    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenNullPaymentsReportingThenReturnNull() {
    ReceiptDetailDTO receiptDetailDTO = ReceiptDetailDTO.builder()
      .debtor(
        PersonDTO.builder().fiscalCode("ABCDEF00B00F205A").fullName("DEBTOR")
          .entityType(PersonEntityType.F).build())
      .paymentDateTime(OffsetDateTime.now())
      .iud("IUD123")
      .debtPositionTypeOrgDescription("DESCRIPTION")
      .pspCompanyName("COMPANY")
      .remittanceInformation("REMITTANCEINFO")
      .paymentAmountCents(1000L)
      .build();

    PaymentsReportingDetailDTO result = mapper.mapToPaymentsReportingDetailDTO(
      null, receiptDetailDTO);

    assertNull(result);
  }

}
