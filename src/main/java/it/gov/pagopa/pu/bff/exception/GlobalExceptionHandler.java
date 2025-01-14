package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidOperatorRoleException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOperatorRoleException(InvalidOperatorRoleException e) {
    ErrorDTO errorDTO = new ErrorDTO("GENERIC_ERROR", e.getMessage());
    return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

