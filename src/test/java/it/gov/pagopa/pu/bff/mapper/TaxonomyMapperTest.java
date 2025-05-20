package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.bff.dto.generated.PagedTaxonomy;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelTaxonomy;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class TaxonomyMapperTest {

  private final TaxonomyMapper mapper = new TaxonomyMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedTaxonomyThenCorrectMapping() {
    PagedModelTaxonomy pagedModelTaxonomy = podamFactory.manufacturePojo(PagedModelTaxonomy.class);

    PagedTaxonomy result = mapper.mapToPagedTaxonomy(pagedModelTaxonomy);

    assertNotNull(result);
    assertEquals(pagedModelTaxonomy.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelTaxonomy.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelTaxonomy.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelTaxonomy.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelTaxonomy.getEmbedded().getTaxonomies(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapToPagedTaxonomyThenPartialMapping() {
    PagedModelTaxonomy pagedModelTaxonomy = podamFactory.manufacturePojo(PagedModelTaxonomy.class);
    pagedModelTaxonomy.getEmbedded().setTaxonomies(Collections.emptyList());

    PagedTaxonomy result = mapper.mapToPagedTaxonomy(pagedModelTaxonomy);

    assertNotNull(result);
    assertEquals(pagedModelTaxonomy.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelTaxonomy.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelTaxonomy.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelTaxonomy.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedTaxonomyThenPartialMapping() {
    PagedModelTaxonomy pagedModelTaxonomy = podamFactory.manufacturePojo(PagedModelTaxonomy.class);
    pagedModelTaxonomy.setPage(null);

    PagedTaxonomy result = mapper.mapToPagedTaxonomy(pagedModelTaxonomy);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModelTaxonomy.getEmbedded().getTaxonomies(), result.getContent());
  }
}
