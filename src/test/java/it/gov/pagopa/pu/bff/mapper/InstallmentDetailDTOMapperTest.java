package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InstallmentDetailDTOMapperTest {
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private InstallmentDetailDTOMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new InstallmentDetailDTOMapper();
  }


  @Test
  void givenInstallmentDetailDTOWithNullFieldsWhenMapToInstallmentDetailDTOThenStatusUnpaid() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.class);
    installmentDetailDTO.setStatus(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.UNPAID);
    installmentDetailDTO.setPaymentDateTime(null);
    installmentDetailDTO.setPayer(null);
    installmentDetailDTO.setPspCompanyName(null);
    installmentDetailDTO.setIur(null);
    installmentDetailDTO.setIud(null);

    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(installmentDetailDTO);

    TestUtils.checkNotNullFields(result, "paymentDateTime", "payer", "pspCompanyName", "iur", "iud");

    assertNotNull(result);
    assertEquals(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.UNPAID, result.getStatus());
    assertNull(result.getPaymentDateTime());
    assertNull(result.getPayer());
    assertNull(result.getPspCompanyName());
    assertNull(result.getIur());
    assertNull(result.getIud());
    TestUtils.reflectionEqualsByName(installmentDetailDTO, result);
  }

  @Test
  void givenInstallmentDetailDTOWithNonNullFieldsWhenMapToInstallmentDetailDTOThenStatusPaid() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.class);
    installmentDetailDTO.setStatus(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.PAID);

    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(installmentDetailDTO);

    TestUtils.checkNotNullFields(result);

    assertNotNull(result);
    assertEquals(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.PAID, result.getStatus());
    TestUtils.reflectionEqualsByName(installmentDetailDTO, result);
  }


  @Test
  void givenNoInstallmentDetailDTOThenNullResult() {
    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(null);

    assertNull(result);
  }

}
