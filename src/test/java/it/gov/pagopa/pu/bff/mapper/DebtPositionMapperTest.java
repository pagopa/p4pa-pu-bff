package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.PaymentOptionsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;
import java.util.stream.Stream;

import static it.gov.pagopa.pu.bff.util.Constants.INSTALLMENT_REMITTANCE_INFORMATION_PLACEHOLDER;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class DebtPositionMapperTest {
  private DebtPositionMapper mapper;
  @Mock
  private PaymentOptionsMapper paymentOptionsMapperMock;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new DebtPositionMapper(paymentOptionsMapperMock);
  }

  @Test
  void givenNoMultiDebtorDebtPositionWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    InstallmentDTO installment1 = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment1.setDueDate(LocalDate.now().plusDays(3));
    InstallmentDTO installment2 = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment2.setDueDate(null);
    InstallmentDTO installment3 = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment3.setDueDate(LocalDate.now());
    InstallmentDTO installment4 = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment4.setDueDate(LocalDate.now().minusDays(2));
    paymentOption.setInstallments(List.of(installment1, installment2, installment3, installment4));

    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(false);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption));

    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    PaymentOptionsExtendedDTO paymentOptionsExtendedDTO = podamFactory.manufacturePojo(PaymentOptionsExtendedDTO.class);
    paymentOptionsExtendedDTO.setInstallments(List.of(installment4, installment3, installment1, installment2));

    Mockito.when(paymentOptionsMapperMock.mapToExtended(List.of(paymentOption)))
      .thenReturn(List.of(paymentOptionsExtendedDTO));

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor());

    // Assert right Installments order
    Assertions.assertEquals(installment4.getInstallmentId(), result.getPaymentOptions().getFirst().getInstallments().get(0).getInstallmentId());
    Assertions.assertEquals(installment3.getInstallmentId(), result.getPaymentOptions().getFirst().getInstallments().get(1).getInstallmentId());
    Assertions.assertEquals(installment1.getInstallmentId(), result.getPaymentOptions().getFirst().getInstallments().get(2).getInstallmentId());
    Assertions.assertEquals(installment2.getInstallmentId(), result.getPaymentOptions().getFirst().getInstallments().get(3).getInstallmentId());

    verifyDebtPositionDetailDTO(result,debtPositionDTO,debtPositionTypeOrg);
  }

  @Test
  void givenNoMultiDebtorDebtPositionAndNulDebtPositionTypeOrgWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(false);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption,paymentOption));

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,null);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result,"debtPositionTypeOrgCode", "debtPositionTypeOrgDescription");
    TestUtils.checkNotNullFields(result.getDebtor());
    verifyDebtPositionDetailDTO(result,debtPositionDTO,null);
  }

  @Test
  void givenMultiDebtorDebtPositionWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(true);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption,paymentOption));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    verifyDebtPositionDetailDTO(result,debtPositionDTO,debtPositionTypeOrg);
  }

  @ParameterizedTest
  @MethodSource("provideRemittanceInformation")
  void givenInstallmentWithVariousRemittanceInformationWhenResolveRemittanceInformationThenCorrectValueIsSet(
    String remittanceInformation,
    String originalRemittanceInformation,
    String expectedTransferRemittanceInformation,
    String expectedInstallmentRemittanceInformation) {

    TransferDTO transfer = podamFactory.manufacturePojo(TransferDTO.class);
    transfer.setRemittanceInformation(expectedTransferRemittanceInformation);
    InstallmentDTO installment = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment.transfers(List.of(transfer));
    installment.setRemittanceInformation(expectedInstallmentRemittanceInformation);
    installment.setOriginalRemittanceInformation(originalRemittanceInformation);
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    paymentOption.setInstallments(List.of(installment));
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(false);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption));

    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    PaymentOptionsExtendedDTO paymentOptionsExtendedDTO = podamFactory.manufacturePojo(PaymentOptionsExtendedDTO.class);
    paymentOptionsExtendedDTO.setInstallments(List.of(installment));

    Mockito.when(paymentOptionsMapperMock.mapToExtended(anyList()))
      .thenReturn(List.of(paymentOptionsExtendedDTO));

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedTransferRemittanceInformation, result.getPaymentOptions().getFirst().getInstallments().getFirst().getTransfers().getFirst().getRemittanceInformation());

    String installmentRemittanceInformation = result.getPaymentOptions().getFirst().getInstallments().getFirst().getRemittanceInformation();
    Assertions.assertEquals(expectedInstallmentRemittanceInformation, installmentRemittanceInformation);
  }

  private void verifyDebtPositionDetailDTO(DebtPositionDetailDTO result, DebtPositionDTO debtPositionDTO,
    DebtPositionTypeOrg debtPositionTypeOrg) {
    if(debtPositionTypeOrg!=null){
      Assertions.assertEquals(debtPositionTypeOrg.getDescription(),result.getDebtPositionTypeOrgDescription());
      Assertions.assertEquals(debtPositionTypeOrg.getCode(),result.getDebtPositionTypeOrgCode());
    }else{
      Assertions.assertNull(result.getDebtPositionTypeOrgCode());
      Assertions.assertNull(result.getDebtPositionTypeOrgDescription());
    }
    TestUtils.reflectionEqualsByName(debtPositionDTO,result, "paymentOptions");
    verifyDebtor(result.getDebtor(),debtPositionDTO);
  }

  private void verifyDebtor(PersonDTO debtor,
    DebtPositionDTO debtPositionDTO) {
    if(Boolean.TRUE.equals(debtPositionDTO.getMultiDebtor())){
      Assertions.assertEquals("CO-OBBLIGATO",debtor.getFullName());
      Assertions.assertTrue(StringUtils.isEmpty(debtor.getFiscalCode()));
      Assertions.assertEquals(PersonEntityType.F,debtor.getEntityType());
    }else{
      TestUtils.reflectionEqualsByName(debtor,debtPositionDTO.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor());
    }
  }

  private static Stream<Arguments> provideRemittanceInformation() {
    return Stream.of(
      Arguments.of("remittanceInformation", null, "remittanceInformation", "remittanceInformation"),
      Arguments.of("remittanceInformation", "originalRemittanceInformation", "remittanceInformation", "originalRemittanceInformation"),
      Arguments.of(INSTALLMENT_REMITTANCE_INFORMATION_PLACEHOLDER +" with remittanceInformation", "originalRemittanceInformation", "originalRemittanceInformation", "originalRemittanceInformation")
    );
  }
}
