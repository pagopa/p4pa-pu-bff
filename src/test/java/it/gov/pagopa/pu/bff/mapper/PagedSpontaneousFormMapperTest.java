package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

class PagedSpontaneousFormMapperTest {

  private final PagedSpontaneousFormMapper mapper = Mappers.getMapper(PagedSpontaneousFormMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapThenCorrectMapping() {
    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(
      PagedModelSpontaneousForm.class);

    PagedSpontaneousForm result = mapper.map(pagedModelSpontaneousForm);

    assertNotNull(result);
    assertEquals(pagedModelSpontaneousForm.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelSpontaneousForm.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelSpontaneousForm.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelSpontaneousForm.getPage().getSize(),
      result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelSpontaneousForm.getEmbedded().getSpontaneousForms(),
      result.getContent());
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoContentWhenMapThenPartialMapping() {
    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(
        PagedModelSpontaneousForm.class);
    pagedModelSpontaneousForm.getEmbedded()
      .setSpontaneousForms(Collections.emptyList());

    PagedSpontaneousForm result = mapper.map(pagedModelSpontaneousForm);

    assertNotNull(result);
    assertEquals(pagedModelSpontaneousForm.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelSpontaneousForm.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelSpontaneousForm.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelSpontaneousForm.getPage().getSize(),
      result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoPageWhenMapThenPartialMapping() {
    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(
        PagedModelSpontaneousForm.class);
    pagedModelSpontaneousForm.setPage(null);

    PagedSpontaneousForm result = mapper.map(pagedModelSpontaneousForm);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelSpontaneousForm.getEmbedded().getSpontaneousForms(),
      result.getContent());
    TestUtils.checkNotNullFields(result, "size", "totalElements", "totalPages", "number");
  }
}
