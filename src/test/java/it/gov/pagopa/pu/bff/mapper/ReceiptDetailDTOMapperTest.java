package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class ReceiptDetailDTOMapperTest {
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private ReceiptDetailDTOMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ReceiptDetailDTOMapper();
  }

  @Test
  void givenPopulatedReceiptDetailWhenMapToReceiptDetailDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO.class);

    ReceiptDetailDTO result = mapper.mapToReceiptDetailDTO(receiptDetailDTO);

    Assertions.assertNotNull(result);
    TestUtils.reflectionEqualsByName(receiptDetailDTO, result, "debtor", "payer");
    TestUtils.reflectionEqualsByName(receiptDetailDTO.getDebtor(), result.getDebtor());
    TestUtils.reflectionEqualsByName(receiptDetailDTO.getPayer(), result.getPayer());
    TestUtils.checkNotNullFields(result);
    TestUtils.checkNotNullFields(result.getDebtor());
    TestUtils.checkNotNullFields(result.getPayer());
  }

  @Test
  void givenNoReceiptDetailDTOThenNullResult() {
    ReceiptDetailDTO result = mapper.mapToReceiptDetailDTO(null);

    Assertions.assertNull(result);
  }

}
