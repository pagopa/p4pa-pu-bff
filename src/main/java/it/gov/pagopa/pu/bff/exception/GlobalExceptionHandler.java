package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO.CategoryEnum;
import it.gov.pagopa.pu.bff.exception.common.CommonExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends CommonExceptionHandler {

  @ExceptionHandler(InvalidAssessmentsRegistryException.class)
  public ResponseEntity<ErrorDTO> handleInvalidAssessmentRegistryException(InvalidAssessmentsRegistryException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidOrganizationException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOrganizationException(InvalidOrganizationException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDebtPositionTypeOrgException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDebtPositionTypeOrgException(InvalidDebtPositionTypeOrgException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidOperatorRoleException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOperatorRoleException(InvalidOperatorRoleException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.GENERIC_ERROR);
  }

  @ExceptionHandler(InvalidOrgSubUnitException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOrgSubUnitException(InvalidOrgSubUnitException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }


  @ExceptionHandler(InvalidDebtPositionException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDebtPositionException(InvalidDebtPositionException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(ZipFileException.class)
  public ResponseEntity<ErrorDTO> handleZipFileException(ZipFileException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, CategoryEnum.GENERIC_ERROR);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidOrgSilServiceException.class)
  public  ResponseEntity<ErrorDTO> handleInvalidOrgSilServiceException(InvalidOrgSilServiceException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.BAD_REQUEST, CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidAssessmentsDetailException.class})
  public ResponseEntity<ErrorDTO> handleInvalidAssessmentsDetailException(InvalidAssessmentsDetailException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidAccessTokenException.class})
  public ResponseEntity<ErrorDTO> handleInvalidAccessTokenException(InvalidAccessTokenException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidUserInfoException.class})
  public ResponseEntity<ErrorDTO> handleInvalidUserInfoException(InvalidUserInfoException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, CategoryEnum.BAD_REQUEST);
  }

}

