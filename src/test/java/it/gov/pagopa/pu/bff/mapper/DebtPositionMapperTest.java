package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PersonDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionMapperTest {
  @Spy
  private PersonDTOMapper personDTOMapperSpy;
  @Mock
  private PaymentOptionMapper paymentOptionMapperMock;
  private DebtPositionMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new DebtPositionMapper(personDTOMapperSpy, paymentOptionMapperMock);
  }

  @Test
  void givenNoMultiDebtorDebtPositionWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(false);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    Mockito.when(paymentOptionMapperMock.mapToPaymentOptionDTO(paymentOption)).thenReturn(
      new it.gov.pagopa.pu.bff.dto.generated.PaymentOptionDTO());

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor());
    verifyDebtPositionDetailDTO(result,debtPositionDTO,debtPositionTypeOrg);
    Mockito.verify(personDTOMapperSpy).mapToPersonDTO(debtPositionDTO.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor());
    Mockito.verify(paymentOptionMapperMock).mapToPaymentOptionDTO(paymentOption);
  }

  @Test
  void givenNoMultiDebtorDebtPositionAndNulDebtPositionTypeOrgWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(false);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption,paymentOption));

    Mockito.when(paymentOptionMapperMock.mapToPaymentOptionDTO(paymentOption)).thenReturn(
      new it.gov.pagopa.pu.bff.dto.generated.PaymentOptionDTO());

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,null);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result,"debtPositionTypeOrgCode", "debtPositionTypeOrgDescription");
    TestUtils.checkNotNullFields(result.getDebtor());
    verifyDebtPositionDetailDTO(result,debtPositionDTO,null);
    Mockito.verify(personDTOMapperSpy).mapToPersonDTO(debtPositionDTO.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor());
    Mockito.verify(paymentOptionMapperMock,Mockito.times(2)).mapToPaymentOptionDTO(paymentOption);
  }

  @Test
  void givenMultiDebtorDebtPositionWhenMapToDebtPositionDetailDTOThenCorrectMapping() {
    PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setMultiDebtor(true);
    debtPositionDTO.setPaymentOptions(List.of(paymentOption,paymentOption));
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);

    Mockito.when(paymentOptionMapperMock.mapToPaymentOptionDTO(paymentOption)).thenReturn(
      new it.gov.pagopa.pu.bff.dto.generated.PaymentOptionDTO());

    DebtPositionDetailDTO result = mapper.mapToDebtPositionDetailDTO(debtPositionDTO,debtPositionTypeOrg);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor(), "fiscalCode");
    verifyDebtPositionDetailDTO(result,debtPositionDTO,debtPositionTypeOrg);
    Mockito.verify(paymentOptionMapperMock,Mockito.times(2)).mapToPaymentOptionDTO(paymentOption);
    Mockito.verifyNoInteractions(personDTOMapperSpy);
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
    Assertions.assertEquals(debtPositionDTO.getPaymentOptions().size(),result.getPaymentOptions().size());
    verifyDebtor(result.getDebtor(),debtPositionDTO);
  }

  private void verifyDebtor(PersonDTO debtor,
    DebtPositionDTO debtPositionDTO) {
    if(Boolean.TRUE.equals(debtPositionDTO.getMultiDebtor())){
      Assertions.assertEquals("CO-OBBLIGATO",debtor.getFullName());
      Assertions.assertNull(debtor.getFiscalCode());
    }else{
      TestUtils.reflectionEqualsByName(debtor,debtPositionDTO.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor());
    }
  }
}
