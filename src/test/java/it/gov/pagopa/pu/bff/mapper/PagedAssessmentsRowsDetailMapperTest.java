package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PagedAssessmentsRowsDetailMapperTest {

  private final PagedAssessmentsRowsDetailMapper mapper = Mappers.getMapper(PagedAssessmentsRowsDetailMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedAssessmentsRowsDetailThenCorrectMapping() {
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(
      PagedModelAssessmentsDetail.class);

    PagedAssessmentsRowsDetail result = mapper.map(pagedModelAssessmentsDetail);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsDetail.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelAssessmentsDetail.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelAssessmentsDetail.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelAssessmentsDetail.getPage().getSize(),
      result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelAssessmentsDetail.getEmbedded().getAssessmentsDetails(),
      result.getContent());
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoContentWhenMapToPagedAssessmentsRowsDetailThenPartialMapping() {
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(
      PagedModelAssessmentsDetail.class);
    pagedModelAssessmentsDetail.getEmbedded()
      .setAssessmentsDetails(Collections.emptyList());

    PagedAssessmentsRowsDetail result = mapper.map(pagedModelAssessmentsDetail);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsDetail.getPage().getNumber(),
      result.getNumber());
    assertEquals(pagedModelAssessmentsDetail.getPage().getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedModelAssessmentsDetail.getPage().getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedModelAssessmentsDetail.getPage().getSize(),
      result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoPageWhenMapToPagedAssessmentsRowsDetailThenPartialMapping() {
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(
      PagedModelAssessmentsDetail.class);
    pagedModelAssessmentsDetail.setPage(null);

    PagedAssessmentsRowsDetail result = mapper.map(pagedModelAssessmentsDetail);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
      pagedModelAssessmentsDetail.getEmbedded().getAssessmentsDetails(),
      result.getContent());
    TestUtils.checkNotNullFields(result, "size", "totalElements", "totalPages", "number");
  }
}
