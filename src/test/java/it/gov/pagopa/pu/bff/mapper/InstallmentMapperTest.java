package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class InstallmentMapperTest {
  private InstallmentMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new InstallmentMapper();
  }

  @Test
  void whenMapToPaymentOptionDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO installmentDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO.class);

    InstallmentDTO result = mapper.mapToInstallmentDTO(installmentDTO);

    Assertions.assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    TestUtils.reflectionEqualsByName(result,installmentDTO);
  }
}
