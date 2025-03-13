package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreasuryViewMapperTest {

  private final TreasuryViewMapper mapper = new TreasuryViewMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedTreasuryThenCorrectMapping() {
    PagedModelTreasuryView pagedModel = podamFactory.manufacturePojo(PagedModelTreasuryView.class);

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertEquals(pagedModel.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModel.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModel.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModel.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModel.getEmbedded().getTreasuryViews(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedTreasuryThenPartialMapping() {
    PagedModelTreasuryView pagedModel = podamFactory.manufacturePojo(PagedModelTreasuryView.class);
    pagedModel.getEmbedded().setTreasuryViews(Collections.emptyList());

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertEquals(pagedModel.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModel.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModel.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModel.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedTreasuryThenPartialMapping() {
    PagedModelTreasuryView pagedModel = podamFactory.manufacturePojo(PagedModelTreasuryView.class);
    pagedModel.setPage(null);

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModel.getEmbedded().getTreasuryViews(), result.getContent());
  }
}
