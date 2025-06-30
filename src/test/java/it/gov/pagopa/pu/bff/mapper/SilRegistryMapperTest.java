package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedSilRegistry;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SilRegistryMapperTest {

  private final SilRegistryMapper mapper = Mappers.getMapper(SilRegistryMapper.class);
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedSilRegistryThenCorrectMapping() {
    PagedModelSilRegistry pagedModelSilRegistry = podamFactory.manufacturePojo(PagedModelSilRegistry.class);

    PagedSilRegistry result = mapper.mapToPagedSilRegistry(pagedModelSilRegistry);

    assertNotNull(result);
    assertEquals(pagedModelSilRegistry.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelSilRegistry.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelSilRegistry.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelSilRegistry.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelSilRegistry.getEmbedded().getSilRegistries(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedSilRegistryThenPartialMapping() {
    PagedModelSilRegistry pagedModelSilRegistry = podamFactory.manufacturePojo(PagedModelSilRegistry.class);
    pagedModelSilRegistry.getEmbedded().setSilRegistries(Collections.emptyList());

    PagedSilRegistry result = mapper.mapToPagedSilRegistry(pagedModelSilRegistry);

    assertNotNull(result);
    assertEquals(pagedModelSilRegistry.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelSilRegistry.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelSilRegistry.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelSilRegistry.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedSilRegistryThenPartialMapping() {
    PagedModelSilRegistry pagedModelSilRegistry = podamFactory.manufacturePojo(PagedModelSilRegistry.class);
    pagedModelSilRegistry.setPage(null);

    PagedSilRegistry result = mapper.mapToPagedSilRegistry(pagedModelSilRegistry);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelSilRegistry.getEmbedded().getSilRegistries(), result.getContent());
  }
}
