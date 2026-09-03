package it.gov.pagopa.pu.bff.connector.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationErrorDTO;
import it.gov.pagopa.pu.bff.config.rest.PuErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorFieldDTO;

public class ClassificationErrorDTOMapper {

  private ClassificationErrorDTOMapper() {
    /* This utility class should not be instantiated */
  }


  public static PuErrorDTO map(ClassificationErrorDTO errorDTO) {
    return new PuErrorDTO(
      errorDTO.getCategory().getValue(),
      errorDTO.getCode(),
      errorDTO.getMessage(),
      errorDTO.getFields() != null
        ? errorDTO.getFields().stream()
        .map(field -> new ErrorFieldDTO(
          field.getField(),
          field.getError(),
          field.getMessage()
        ))
        .toList()
        : null
    );
  }
}
