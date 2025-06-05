package it.gov.pagopa.pu.bff.mapper;


import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class DebtPositionTypeOrgDTOMapperTest {

  private final DebtPositionTypeOrgDTOMapper mapper = Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class);

  @Test
  void givenDtoWhenMapThenMapIt(){
    // Given
    DebtPositionTypeOrg dto = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);

    // When
    DebtPositionTypeOrgDTO result = mapper.map(dto, "DebtPositionTypeDescription", "DebtPositionTypeCode");

    // Then
    TestUtils.reflectionEqualsByName(dto, result);
    TestUtils.checkNotNullFields(result);
  }
}
