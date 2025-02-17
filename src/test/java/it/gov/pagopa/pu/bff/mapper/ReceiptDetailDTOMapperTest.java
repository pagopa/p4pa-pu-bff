package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class ReceiptDetailDTOMapperTest {

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final ReceiptDetailDTOMapper mapper = new ReceiptDetailDTOMapper();

  @Test
  void givenPopulatedReceiptDetailWhenMapToReceiptDetailDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO.class);

    ReceiptDetailDTO result = mapper.mapToReceiptDetailDTO(receiptDetailDTO);

    Assertions.assertNotNull(result);
    TestUtils.reflectionEqualsByName(receiptDetailDTO, result);
    Assertions.assertEquals(receiptDetailDTO.getDebtor().getFullName(),result.getDebtorFullName());
    Assertions.assertEquals(receiptDetailDTO.getDebtor().getFiscalCode(),result.getDebtorFiscalCode());
    Assertions.assertEquals(receiptDetailDTO.getPayer().getFullName(),result.getPayerFullName());
    Assertions.assertEquals(receiptDetailDTO.getPayer().getFiscalCode(),result.getPayerFiscalCode());
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoPayerWhenMapToReceiptDetailDTOThenResultWithNoPayer() {
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO.class);
    receiptDetailDTO.setPayer(null);

    ReceiptDetailDTO result = mapper.mapToReceiptDetailDTO(receiptDetailDTO);

    Assertions.assertNotNull(result);
    TestUtils.reflectionEqualsByName(receiptDetailDTO, result);
    Assertions.assertEquals(receiptDetailDTO.getDebtor().getFullName(),result.getDebtorFullName());
    Assertions.assertEquals(receiptDetailDTO.getDebtor().getFiscalCode(),result.getDebtorFiscalCode());
    Assertions.assertNull(result.getPayerFullName());
    Assertions.assertNull(result.getPayerFiscalCode());
    TestUtils.checkNotNullFields(result,"payerFullName","payerFiscalCode");
  }

  @Test
  void givenNoReceiptDetailDTOThenNullResult() {
    ReceiptDetailDTO result = mapper.mapToReceiptDetailDTO(null);

    Assertions.assertNull(result);
  }

}
