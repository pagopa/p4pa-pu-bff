package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PagoPaRegistryMapperTest {

  private final PagoPaRegistryMapper mapper = Mappers.getMapper(PagoPaRegistryMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPagoPaRegistryThenCorrectMapping() {
    PagedModelPagoPaRegistry pagedModelPagoPaRegistry = podamFactory.manufacturePojo(
            PagedModelPagoPaRegistry.class);

    PagedPagoPaRegistry result = mapper.mapToPagedPagoPaRegistry(
            pagedModelPagoPaRegistry);

    assertNotNull(result);
    assertEquals(pagedModelPagoPaRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelPagoPaRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelPagoPaRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelPagoPaRegistry.getPage().getSize(),
            result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            pagedModelPagoPaRegistry.getEmbedded().getPagoPaRegistries(),
            result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedPagoPaRegistryThenPartialMapping() {
    PagedModelPagoPaRegistry pagedModelPagoPaRegistry = podamFactory.manufacturePojo(
            PagedModelPagoPaRegistry.class);
    pagedModelPagoPaRegistry.getEmbedded()
            .setPagoPaRegistries(Collections.emptyList());

    PagedPagoPaRegistry result = mapper.mapToPagedPagoPaRegistry(
            pagedModelPagoPaRegistry);

    assertNotNull(result);
    assertEquals(pagedModelPagoPaRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelPagoPaRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelPagoPaRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelPagoPaRegistry.getPage().getSize(),
            result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedPagoPaRegistryThenPartialMapping() {
    PagedModelPagoPaRegistry pagedModelPagoPaRegistry = podamFactory.manufacturePojo(
            PagedModelPagoPaRegistry.class);
    pagedModelPagoPaRegistry.setPage(null);

    PagedPagoPaRegistry result = mapper.mapToPagedPagoPaRegistry(
            pagedModelPagoPaRegistry);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            pagedModelPagoPaRegistry.getEmbedded().getPagoPaRegistries(),
            result.getContent());
  }
}
