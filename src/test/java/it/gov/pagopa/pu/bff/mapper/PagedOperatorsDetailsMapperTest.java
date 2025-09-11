package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedOperatorsDetails;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

class PagedOperatorsDetailsMapperTest {

  private PodamFactory podamFactory =  TestUtils.getPodamFactory();
  PagedOperatorsDetailsMapper mapper = Mappers.getMapper(PagedOperatorsDetailsMapper.class);

  @Test
  void givenPagedModelDebtPositionTypeOrgWhenMapThenReturnPagedOperatorsDetails() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    //when
    PagedOperatorsDetails result = mapper.map(pagedModelDebtPositionTypeOrg);
    //then
    Assertions.assertNotNull(result);

    for (int i = 0; i < pagedModelDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs().size(); i++){
      TestUtils.reflectionEqualsByName(pagedModelDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs().get(i), result.getContent().get(i));
      TestUtils.checkNotNullFields(result);
    }

  }
}
