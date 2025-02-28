package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InstallmentDetailDTOMapperTest {
  private InstallmentDetailDTOMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new InstallmentDetailDTOMapper();
  }

  @Test
  void givenNoInstallmentDetailDTOThenNullResult() {
    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(null);

    Assertions.assertNull(result);
  }

  @Test
  void givenInstallmentDetailDTOWithNullFieldsWhenMapToInstallmentDetailDTOThenStatusUnpaid() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = new it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO();
    installmentDetailDTO.setStatus(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.UNPAID);

    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(installmentDetailDTO);

    Assertions.assertNotNull(result);
    assertEquals(InstallmentDetailDTO.StatusEnum.UNPAID, result.getStatus());
  }

  @Test
  void givenInstallmentDetailDTOWithNonNullFieldsWhenMapToInstallmentDetailDTOThenStatusPaid() {
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO = new it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO();
    installmentDetailDTO.setStatus(it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.PAID);
    installmentDetailDTO.setPayer(new InstallmentDetailDTO().getPayer());
    installmentDetailDTO.setPaymentDateTime(OffsetDateTime.parse("2025-02-28T16:00:00Z"));
    installmentDetailDTO.setIud("someIud");
    installmentDetailDTO.setIur("someIur");
    installmentDetailDTO.setPspCompanyName("somePspCompanyName");

    InstallmentDetailDTO result = mapper.mapToInstallmentDetailDTO(installmentDetailDTO);

    Assertions.assertNotNull(result);
    assertEquals(InstallmentDetailDTO.StatusEnum.PAID, result.getStatus());
  }

}
