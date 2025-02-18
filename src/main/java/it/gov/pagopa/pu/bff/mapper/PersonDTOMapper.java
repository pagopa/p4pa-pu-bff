package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PersonDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonDTOMapper {

  public PersonDTO mapToPersonDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO person) {
    if(person==null){
      return null;
    }
    return PersonDTO.builder()
      .fullName(person.getFullName())
      .fiscalCode(person.getFiscalCode())
      .build();
  }
}
