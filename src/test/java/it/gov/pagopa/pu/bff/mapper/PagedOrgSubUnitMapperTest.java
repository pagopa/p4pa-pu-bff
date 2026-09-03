package it.gov.pagopa.pu.bff.mapper;


import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PagedOrgSubUnitMapperTest {
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  PagedOrgSubUnitMapper mapper = Mappers.getMapper(PagedOrgSubUnitMapper.class);

  @Test
  void givenPagedModelOrgSubUnitWhenMapThenOk() {
    PagedModelOrgSubUnit source = podamFactory.manufacturePojo(PagedModelOrgSubUnit.class);

    PagedOrgSubUnit result = mapper.map(source);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    assertEquals(source.getEmbedded().getOrgSubUnits(), result.getContent());
    assertEquals(source.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(source.getPage().getSize(), result.getSize());
    assertEquals(source.getPage().getNumber(), result.getNumber());
    assertEquals(source.getPage().getTotalElements(), result.getTotalElements());
  }
}
