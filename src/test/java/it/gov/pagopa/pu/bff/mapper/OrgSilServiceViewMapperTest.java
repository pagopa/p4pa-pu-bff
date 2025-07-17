package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrgSilServiceViewMapperTest {

  private final OrgSilServiceViewMapper mapper = Mappers.getMapper(OrgSilServiceViewMapper.class);
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapThenCorrectMapping() {
    PagedModelOrgSilServiceView pagedModel = podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);

    PagedOrgSilServiceView result = mapper.map(pagedModel);

    assertNotNull(result);
    assertEquals(pagedModel.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModel.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModel.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModel.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModel.getEmbedded().getOrgSilServiceViews(), result.getContent());
  }

  @Test
  void givenNoContentWhenMapThenEmptyContent() {
    PagedModelOrgSilServiceView pagedModel = podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);
    pagedModel.getEmbedded().setOrgSilServiceViews(Collections.emptyList());

    PagedOrgSilServiceView result = mapper.map(pagedModel);

    assertNotNull(result);
    assertEquals(pagedModel.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModel.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModel.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModel.getPage().getSize(), result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapThenPartialMapping() {
    PagedModelOrgSilServiceView pagedModel = podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);
    pagedModel.setPage(null);

    PagedOrgSilServiceView result = mapper.map(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(pagedModel.getEmbedded().getOrgSilServiceViews(), result.getContent());
  }

  @Test
  void givenNullEmbeddedWhenMapThenEmptyContent() {
    PagedModelOrgSilServiceView pagedModel = podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);
    pagedModel.setEmbedded(null);

    PagedOrgSilServiceView result = mapper.map(pagedModel);

    assertNotNull(result);
    assertEquals(pagedModel.getPage() != null ? pagedModel.getPage().getNumber() : null, result.getNumber());
    assertEquals(pagedModel.getPage() != null ? pagedModel.getPage().getTotalElements() : null, result.getTotalElements());
    assertEquals(pagedModel.getPage() != null ? pagedModel.getPage().getTotalPages() : null, result.getTotalPages());
    assertEquals(pagedModel.getPage() != null ? pagedModel.getPage().getSize() : null, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }
}
