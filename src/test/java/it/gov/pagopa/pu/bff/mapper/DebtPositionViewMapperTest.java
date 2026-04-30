package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionViewMapperTest {

  private final DebtPositionViewMapper mapper = new DebtPositionViewMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedDebtPositionViewThenCorrectMapping() {
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(PagedModelDebtPositionView.class);

    PagedDebtPositionView result = mapper.mapToPagedDebtPositionView(pagedModelDebtPositionView);

    assertNotNull(result);
    assertEquals(pagedModelDebtPositionView.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelDebtPositionView.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelDebtPositionView.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelDebtPositionView.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelDebtPositionView.getEmbedded().getDebtPositionViews(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(PagedModelDebtPositionView.class);
    pagedModelDebtPositionView.getEmbedded().setDebtPositionViews(Collections.emptyList());

    PagedDebtPositionView result = mapper.mapToPagedDebtPositionView(pagedModelDebtPositionView);

    assertNotNull(result);
    assertEquals(pagedModelDebtPositionView.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelDebtPositionView.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelDebtPositionView.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelDebtPositionView.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(PagedModelDebtPositionView.class);
    pagedModelDebtPositionView.setPage(null);

    PagedDebtPositionView result = mapper.mapToPagedDebtPositionView(pagedModelDebtPositionView);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelDebtPositionView.getEmbedded().getDebtPositionViews(), result.getContent());
  }
}
