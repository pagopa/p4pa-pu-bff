package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.PageMetadata;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryViewEmbedded;
import it.gov.pagopa.pu.classification.dto.generated.TreasuryView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreasuryViewMapperTest {

  private final TreasuryViewMapper mapper = new TreasuryViewMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedTreasuryThenCorrectMapping() {
    PagedModelTreasuryView pagedModel = new PagedModelTreasuryView();
    PagedModelTreasuryViewEmbedded embedded = new PagedModelTreasuryViewEmbedded();
    TreasuryView treasuryView = new TreasuryView();
    treasuryView.setTreasuryId("1");

    embedded.setTreasuryViews(List.of(treasuryView));
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(treasuryView.getTreasuryId(), result.getContent().get(0).getTreasuryId());
  }

  @Test
  void givenNoContentWhenMapToPagedTreasuryThenPartialMapping() {
    PagedModelTreasuryView pagedModel = new PagedModelTreasuryView();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedTreasuryThenPartialMapping() {
    PagedModelTreasuryView pagedModel = new PagedModelTreasuryView();
    PagedModelTreasuryViewEmbedded embedded = new PagedModelTreasuryViewEmbedded();
    TreasuryView treasuryView = new TreasuryView();
    treasuryView.setTreasuryId("1");

    embedded.setTreasuryViews(List.of(treasuryView));
    pagedModel.setEmbedded(embedded);

    PagedTreasuryView result = mapper.mapToPagedTreasury(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(treasuryView.getTreasuryId(), result.getContent().get(0).getTreasuryId());
  }
}
