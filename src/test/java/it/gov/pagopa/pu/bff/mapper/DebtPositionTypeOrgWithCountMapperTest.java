package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCountEmbedded;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgWithCountMapperTest {

  private final DebtPositionTypeOrgWithCountMapper mapper = new DebtPositionTypeOrgWithCountMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedDebtPositionTypeOrgWithCountThenCorrectMapping() {
    PagedModelDebtPositionTypeOrgWithCount pagedModel = new PagedModelDebtPositionTypeOrgWithCount();
    PagedModelDebtPositionTypeOrgWithCountEmbedded embedded = new PagedModelDebtPositionTypeOrgWithCountEmbedded();
    DebtPositionTypeOrgWithCount debtPositionTypeOrgWithCount = new DebtPositionTypeOrgWithCount();

    embedded.setDebtPositionTypeOrgWithCounts(List.of(debtPositionTypeOrgWithCount));
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedDebtPositionTypeOrgWithCount result = mapper.mapToPagedDebtPositionTypeOrgWithCount(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertSame(debtPositionTypeOrgWithCount, result.getContent().get(0));
  }

  @Test
  void givenEmptyModelWhenMapToPagedDebtPositionTypeOrgWithCountThenPartialMapping() {
    PagedModelDebtPositionTypeOrgWithCount pagedModel = new PagedModelDebtPositionTypeOrgWithCount();

    PagedDebtPositionTypeOrgWithCount result = mapper.mapToPagedDebtPositionTypeOrgWithCount(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenPagedModelWithEmptyEmbeddedWhenMapToPagedDebtPositionTypeOrgWithCountThenEmptyContent() {
    PagedModelDebtPositionTypeOrgWithCount pagedModel = new PagedModelDebtPositionTypeOrgWithCount();
    PagedModelDebtPositionTypeOrgWithCountEmbedded embedded = new PagedModelDebtPositionTypeOrgWithCountEmbedded();
    embedded.setDebtPositionTypeOrgWithCounts(Collections.emptyList());
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(0L);
    page.setTotalPages(0L);
    page.setNumber(0L);
    pagedModel.setPage(page);

    PagedDebtPositionTypeOrgWithCount result = mapper.mapToPagedDebtPositionTypeOrgWithCount(pagedModel);

    assertNotNull(result);
    assertEquals(0L, result.getNumber());
    assertEquals(0L, result.getTotalElements());
    assertEquals(0L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

}
