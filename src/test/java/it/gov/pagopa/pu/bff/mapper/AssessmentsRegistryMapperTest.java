package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentsRegistryMapperTest {

  private final AssessmentsRegistryMapper mapper = Mappers.getMapper(AssessmentsRegistryMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPaymentsReportingThenCorrectMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries(),
            result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    pagedModelAssessmentsRegistry.getEmbedded()
            .setAssessmentsRegistries(Collections.emptyList());

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionViewThenPartialMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    pagedModelAssessmentsRegistry.setPage(null);

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries(),
            result.getContent());
  }
}
