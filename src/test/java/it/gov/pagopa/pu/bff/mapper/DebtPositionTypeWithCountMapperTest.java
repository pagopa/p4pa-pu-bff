package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCount;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCountEmbedded;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeWithCountMapperTest {

  private final DebtPositionTypeWithCountMapper mapper = new DebtPositionTypeWithCountMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedDebtPositionWithCountThenCorrectMapping() {
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedModelDebtPositionTypeWithCountEmbedded embedded = new PagedModelDebtPositionTypeWithCountEmbedded();
    DebtPositionTypeWithCount debtPositionTypeWithCount = new DebtPositionTypeWithCount();
    debtPositionTypeWithCount.setDebtPositionTypeId(1L);
    debtPositionTypeWithCount.setCode("code");
    debtPositionTypeWithCount.setDescription("description");
    debtPositionTypeWithCount.setUpdateDate(OffsetDateTime.now());
    debtPositionTypeWithCount.setActiveOrganizations(10);
    embedded.setDebtPositionTypeWithCounts(List.of(debtPositionTypeWithCount));
    pagedModelDebtPositionTypeWithCount.setEmbedded(embedded);
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModelDebtPositionTypeWithCount.setPage(page);

    PagedDebtPositionTypeWithCount result = mapper.mapToPagedDebtPositionWithCount(
      pagedModelDebtPositionTypeWithCount);

    TestUtils.checkNotNullFields(result);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(debtPositionTypeWithCount.getDebtPositionTypeId(), result.getContent().getFirst().getDebtPositionTypeId());
    assertEquals(debtPositionTypeWithCount.getCode(), result.getContent().getFirst().getCode());
    assertEquals(debtPositionTypeWithCount.getDescription(), result.getContent().getFirst().getDescription());
    assertEquals(debtPositionTypeWithCount.getUpdateDate(), result.getContent().getFirst().getUpdateDate());
    assertEquals(debtPositionTypeWithCount.getActiveOrganizations(), result.getContent().getFirst().getActiveOrganizations());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionWithCountThenPartialMapping() {
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModelDebtPositionTypeWithCount.setPage(page);

    PagedDebtPositionTypeWithCount result = mapper.mapToPagedDebtPositionWithCount(
      pagedModelDebtPositionTypeWithCount);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionWithCountThenPartialMapping() {
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedModelDebtPositionTypeWithCountEmbedded embedded = new PagedModelDebtPositionTypeWithCountEmbedded();
    DebtPositionTypeWithCount debtPositionTypeWithCount = new DebtPositionTypeWithCount();
    debtPositionTypeWithCount.setDebtPositionTypeId(1L);
    debtPositionTypeWithCount.setCode("code");
    debtPositionTypeWithCount.setDescription("description");
    debtPositionTypeWithCount.setUpdateDate(OffsetDateTime.now());
    debtPositionTypeWithCount.setActiveOrganizations(10);
    embedded.setDebtPositionTypeWithCounts(List.of(debtPositionTypeWithCount));
    pagedModelDebtPositionTypeWithCount.setEmbedded(embedded);

    PagedDebtPositionTypeWithCount result = mapper.mapToPagedDebtPositionWithCount(
      pagedModelDebtPositionTypeWithCount);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(debtPositionTypeWithCount.getDebtPositionTypeId(), result.getContent().getFirst().getDebtPositionTypeId());
    assertEquals(debtPositionTypeWithCount.getCode(), result.getContent().getFirst().getCode());
    assertEquals(debtPositionTypeWithCount.getDescription(), result.getContent().getFirst().getDescription());
    assertEquals(debtPositionTypeWithCount.getUpdateDate(), result.getContent().getFirst().getUpdateDate());
    assertEquals(debtPositionTypeWithCount.getActiveOrganizations(), result.getContent().getFirst().getActiveOrganizations());
  }
}
