package it.gov.pagopa.pu.bff.connector.organization.mapper;

import it.gov.pagopa.pu.bff.config.rest.PuErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorFieldDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationErrorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

class OrganizationErrorDTOMapperTest {

  @Test
  void whenMapThenReturnPuErrorDTO() {
    // Given
    OrganizationErrorDTO errorDTO = TestUtils.getPodamFactory().manufacturePojo(OrganizationErrorDTO.class);
    Objects.requireNonNull(errorDTO.getFields());

    // When
    PuErrorDTO result = OrganizationErrorDTOMapper.map(errorDTO);

    // Then
    TestUtils.checkNotNullFields(result);

    Assertions.assertEquals(errorDTO.getCategory().getValue(), result.category());
    Assertions.assertEquals(errorDTO.getCode(), result.code());
    Assertions.assertEquals(errorDTO.getMessage(), result.message());

    Assertions.assertEquals(5, result.fields().size());
    List<ErrorFieldDTO> fields = result.fields();
    for (int i = 0; i < fields.size(); i++) {
      ErrorFieldDTO ef = fields.get(i);
      TestUtils.checkNotNullFields(ef);

      it.gov.pagopa.pu.organization.dto.generated.ErrorFieldDTO expectedErrorFieldDTO = errorDTO.getFields().get(i);
      Assertions.assertEquals(expectedErrorFieldDTO.getField(), ef.getField());
      Assertions.assertEquals(expectedErrorFieldDTO.getError(), ef.getError());
      Assertions.assertEquals(expectedErrorFieldDTO.getMessage(), ef.getMessage());
    }

  }

}
