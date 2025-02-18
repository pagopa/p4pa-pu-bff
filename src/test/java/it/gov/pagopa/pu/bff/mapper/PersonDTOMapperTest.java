package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PersonDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PersonDTOMapperTest {
  private final PersonDTOMapper mapper = new PersonDTOMapper();
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPersonDTOWhenMapToPersonDTOThenCorrectMapping() {
    it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO personDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO.class);

    PersonDTO result = mapper.mapToPersonDTO(personDTO);

    Assertions.assertNotNull(result);
    TestUtils.reflectionEqualsByName(personDTO, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNoReceiptDetailDTOThenNullResult() {
    PersonDTO result = mapper.mapToPersonDTO(null);

    Assertions.assertNull(result);
  }

}
