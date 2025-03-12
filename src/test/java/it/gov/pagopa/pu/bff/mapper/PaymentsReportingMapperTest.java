package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO.EntityTypeEnum;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingMapperTest {

  private final PaymentsReportingMapper mapper = new PaymentsReportingMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPaymentsReportingThenCorrectMapping() {
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReporting.class);

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
      pagedModelPaymentsReporting.getEmbedded().getPaymentsReportings(),
      result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReporting.class);
    pagedModelPaymentsReporting.getEmbedded()
      .setPaymentsReportings(Collections.emptyList());

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
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(
      PagedModelPaymentsReporting.class);
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
      pagedModelPaymentsReporting.getEmbedded().getPaymentsReportings(),
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
      .build();
    ReceiptDetailDTO receiptDetailDTO = ReceiptDetailDTO.builder()
      .debtor(
        PersonDTO.builder().fiscalCode("ABCDEF00B00F205A").fullName("DEBTOR")
          .entityType(EntityTypeEnum.F).build())
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
      .status(StatusEnum.REPORTED)
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
      .build();

    PaymentsReportingDetailDTO expected = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(StatusEnum.REPORTED)
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
          .entityType(EntityTypeEnum.F).build())
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
