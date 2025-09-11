package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorRole;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

class OperatorDetailMapperTest {

  private PodamFactory podamFactory =  TestUtils.getPodamFactory();
  OperatorDetailMapper mapper = Mappers.getMapper(OperatorDetailMapper.class);

  @Test
  void givenPagedModelDebtPositionTypeOrgAndOperatorDTOWhenMapThenReturnOperatorsDetail() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setRoles(List.of("ROLE_ADMIN"));

    //when
    OperatorsDetail result = mapper.map(pagedModelDebtPositionTypeOrg, operatorDTO);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(operatorDTO.getOperatorId(), result.getOperatorId());
    Assertions.assertEquals(operatorDTO.getFiscalCode(), result.getOperatorFiscalCode());
    Assertions.assertEquals(OperatorRole.ROLE_ADMIN, result.getOperatorRole());
    Assertions.assertEquals(pagedModelDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs(), result.getPagedOperatorsDetails().getContent());
    Assertions.assertEquals(pagedModelDebtPositionTypeOrg.getPage().getNumber(), result.getPagedOperatorsDetails().getNumber());
    Assertions.assertEquals(pagedModelDebtPositionTypeOrg.getPage().getTotalPages(), result.getPagedOperatorsDetails().getTotalPages());
    Assertions.assertEquals(pagedModelDebtPositionTypeOrg.getPage().getTotalElements(), result.getPagedOperatorsDetails().getTotalElements());
    Assertions.assertEquals(pagedModelDebtPositionTypeOrg.getPage().getSize(), result.getPagedOperatorsDetails().getSize());

    TestUtils.checkNotNullFields(result);
  }

}
