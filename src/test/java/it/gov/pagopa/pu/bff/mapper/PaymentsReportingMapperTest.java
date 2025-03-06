package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingMapperTest {
  private final PaymentsReportingMapper mapper = new PaymentsReportingMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPaymentsReportingThenCorrectMapping() {
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(PagedModelPaymentsReporting.class);

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(pagedModelPaymentsReporting);

    assertNotNull(result);
    assertEquals(pagedModelPaymentsReporting.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelPaymentsReporting.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelPaymentsReporting.getEmbedded().getPaymentsReportings(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(PagedModelPaymentsReporting.class);
    pagedModelPaymentsReporting.getEmbedded().setPaymentsReportings(Collections.emptyList());

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(pagedModelPaymentsReporting);

    assertNotNull(result);
    assertEquals(pagedModelPaymentsReporting.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelPaymentsReporting.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelPaymentsReporting.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelPaymentsReporting pagedModelPaymentsReporting = podamFactory.manufacturePojo(PagedModelPaymentsReporting.class);
    pagedModelPaymentsReporting.setPage(null);

    PagedPaymentsReportingRow result = mapper.mapToPagedPaymentsReporting(pagedModelPaymentsReporting);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelPaymentsReporting.getEmbedded().getPaymentsReportings(), result.getContent());
  }
}
