package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.PaymentOptionsExtendedDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentOptionsMapperTest {
  private PaymentOptionsMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new PaymentOptionsMapper();
  }

  @Test
  void givenValidPaymentOptionsWhenMapToExtendedThenCorrectMapping() {
    // Given
    PersonDTO expectedDebtor = podamFactory.manufacturePojo(PersonDTO.class);

    InstallmentDTO installment = podamFactory.manufacturePojo(InstallmentDTO.class);
    installment.setDebtor(expectedDebtor);

    PaymentOptionDTO po = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    po.setInstallments(List.of(installment));

    // When
    List<PaymentOptionsExtendedDTO> result = mapper.mapToExtended(List.of(po));

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());

    PaymentOptionsExtendedDTO extended = result.getFirst();
    assertEquals(po.getPaymentOptionId(), extended.getPaymentOptionId());
    assertEquals(po.getTotalAmountCents(), extended.getTotalAmountCents());
    assertEquals(po.getInstallments(), extended.getInstallments());

    assertNotNull(extended.getDebtor());
    assertEquals(expectedDebtor, extended.getDebtor());
  }

  @Test
  void givenPaymentOptionWithNoInstallmentsWhenMapToExtendedThenDebtorIsNull() {
    // Given
    PaymentOptionDTO po = podamFactory.manufacturePojo(PaymentOptionDTO.class);
    po.setInstallments(Collections.emptyList());

    // When
    List<PaymentOptionsExtendedDTO> result = mapper.mapToExtended(List.of(po));

    // Then
    assertNotNull(result);
    assertNull(result.getFirst().getDebtor());
    assertTrue(result.getFirst().getInstallments().isEmpty());
  }

  @Test
  void givenEmptyListWhenMapToExtendedThenReturnEmptyList() {
    // When
    List<PaymentOptionsExtendedDTO> result = mapper.mapToExtended(Collections.emptyList());

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
