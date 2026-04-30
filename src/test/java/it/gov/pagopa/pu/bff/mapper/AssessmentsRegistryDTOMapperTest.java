package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssessmentsRegistryDTOMapperTest {

  private final AssessmentsRegistryDTOMapper mapper = Mappers.getMapper(AssessmentsRegistryDTOMapper.class);

  @Test
  void givenAssessmentsRegistryWhenMapThenMapIt() {
    AssessmentsRegistry inputDTO = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);
    String debtPositionTypeOrgDescription = "debtPositionTypeOrgDescription";

    AssessmentsRegistryDTO outputDTO = mapper.map(inputDTO, debtPositionTypeOrgDescription);

    TestUtils.reflectionEqualsByName(inputDTO, outputDTO);
    TestUtils.checkNotNullFields(outputDTO);
    assertEquals(debtPositionTypeOrgDescription,outputDTO.getDebtPositionTypeOrgDescription());
  }
}
