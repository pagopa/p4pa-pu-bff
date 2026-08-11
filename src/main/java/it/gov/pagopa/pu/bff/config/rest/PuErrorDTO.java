package it.gov.pagopa.pu.bff.config.rest;

import it.gov.pagopa.pu.bff.dto.generated.ErrorFieldDTO;

import java.util.List;

public record PuErrorDTO(
  String category,
  String code,
  String message,
  List<ErrorFieldDTO> fields
) {
}
