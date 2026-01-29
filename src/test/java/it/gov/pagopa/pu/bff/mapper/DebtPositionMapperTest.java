package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DebtPositionMapperTest {
  private DebtPositionMapper mapper;
  private PaymentOptionsMapper paymentOptionsMapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new DebtPositionMapper(paymentOptionsMapper);
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

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor());

    // Assert right Installments order
    Assertions.assertEquals(installment4, result.getPaymentOptions().getFirst().getInstallments().get(0));
    Assertions.assertEquals(installment3, result.getPaymentOptions().getFirst().getInstallments().get(1));
    Assertions.assertEquals(installment1, result.getPaymentOptions().getFirst().getInstallments().get(2));
    Assertions.assertEquals(installment2, result.getPaymentOptions().getFirst().getInstallments().get(3));

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

  private void verifyDebtPositionDetailDTO(DebtPositionDetailDTO result, DebtPositionDTO debtPositionDTO,
    DebtPositionTypeOrg debtPositionTypeOrg) {
    if(debtPositionTypeOrg!=null){
      Assertions.assertEquals(debtPositionTypeOrg.getDescription(),result.getDebtPositionTypeOrgDescription());
      Assertions.assertEquals(debtPositionTypeOrg.getCode(),result.getDebtPositionTypeOrgCode());
    }else{
      Assertions.assertNull(result.getDebtPositionTypeOrgCode());
      Assertions.assertNull(result.getDebtPositionTypeOrgDescription());
    }
    TestUtils.reflectionEqualsByName(debtPositionDTO,result);
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
}
