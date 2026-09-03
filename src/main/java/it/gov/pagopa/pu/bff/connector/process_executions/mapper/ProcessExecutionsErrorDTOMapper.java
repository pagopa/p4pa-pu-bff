package it.gov.pagopa.pu.bff.connector.process_executions.mapper;

import it.gov.pagopa.pu.processexecutions.dto.generated.ProcessExecutionsErrorDTO;
import it.gov.pagopa.pu.bff.config.rest.PuErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorFieldDTO;

public class ProcessExecutionsErrorDTOMapper {

  private ProcessExecutionsErrorDTOMapper() {
    /* This utility class should not be instantiated */
  }


  public static PuErrorDTO map(ProcessExecutionsErrorDTO errorDTO) {
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
