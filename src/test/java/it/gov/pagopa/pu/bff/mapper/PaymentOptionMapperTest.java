package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDTO;
import it.gov.pagopa.pu.bff.dto.generated.PaymentOptionDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PaymentOptionMapperTest {
  @Mock
  private InstallmentMapper installmentMapperMock;
  private PaymentOptionMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new PaymentOptionMapper(installmentMapperMock);
  }

  @Test
  void whenMapToPaymentOptionDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO paymentOption = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO.class);
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO installmentDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO.class);
    paymentOption.setInstallments(List.of(installmentDTO));
    Mockito.when(installmentMapperMock.mapToInstallmentDTO(installmentDTO)).thenReturn(new InstallmentDTO());

    PaymentOptionDTO result = mapper.mapToPaymentOptionDTO(paymentOption);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.reflectionEqualsByName(result,paymentOption, "installments");
    Assertions.assertEquals(paymentOption.getInstallments().size(),result.getInstallments().size());
    Mockito.verify(installmentMapperMock).mapToInstallmentDTO(installmentDTO);
  }
}
