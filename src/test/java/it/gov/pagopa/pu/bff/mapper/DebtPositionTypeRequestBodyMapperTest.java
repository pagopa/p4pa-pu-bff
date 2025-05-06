package it.gov.pagopa.pu.bff.mapper;


import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class DebtPositionTypeRequestBodyMapperTest {

  private final DebtPositionTypeRequestBodyMapper mapper = Mappers.getMapper(DebtPositionTypeRequestBodyMapper.class);

  @Test
  void givenDtoWhenMapThenMapIt(){
    // Given
    DebtPositionTypeRequestBody dto = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeRequestBody.class);

    // When
    it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody result = mapper.map(dto);

    // Then
    TestUtils.reflectionEqualsByName(dto, result);
    Assertions.assertEquals(-1L, result.getBrokerId());
    TestUtils.checkNotNullFields(result, "creationDate", "updateDate", "updateOperatorExternalId", "updateTraceId");
  }

  @Test
  void givenDtoAndBrokerIdWhenMapThenMapIt(){
    // Given
    DebtPositionTypeRequestBody dto = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeRequestBody.class);
    Long brokerId = 10L;

    // When
    it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody result = mapper.map(dto, brokerId);

    // Then
    TestUtils.reflectionEqualsByName(dto, result);
    Assertions.assertSame(brokerId, result.getBrokerId());
    TestUtils.checkNotNullFields(result, "creationDate", "updateDate", "updateOperatorExternalId", "updateTraceId");
  }
}
