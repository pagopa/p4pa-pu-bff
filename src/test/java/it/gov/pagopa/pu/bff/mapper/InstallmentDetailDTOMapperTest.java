package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class InstallmentDetailDTOMapperTest {
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private InstallmentDetailDTOMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new InstallmentDetailDTOMapper();
  }

  @Test
  void givenPopulatedInstallmentDetailWhenMapToInstallmentDetailDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.class);

    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(installmentDetailDTO);

    Assertions.assertNotNull(result);
    TestUtils.reflectionEqualsByName(installmentDetailDTO, result, "debtor", "payer");
    TestUtils.reflectionEqualsByName(installmentDetailDTO.getDebtor(), result.getDebtor());
    TestUtils.reflectionEqualsByName(installmentDetailDTO.getPayer(), result.getPayer());
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor());
    TestUtils.checkNotNullFields(result.getPayer());
  }

  @Test
  void givenNoInstallmentDetailDTOThenNullResult() {
    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(null);

    Assertions.assertNull(result);
  }

}
