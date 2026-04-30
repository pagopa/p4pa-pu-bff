package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO.CategoryEnum;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
import it.gov.pagopa.pu.bff.util.ErrorMessageParser;
import it.gov.pagopa.pu.bff.util.Utilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.event.Level;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private final UpstreamErrorMapper upstreamErrorMapper;

  public GlobalExceptionHandler(UpstreamErrorMapper upstreamErrorMapper) {
    this.upstreamErrorMapper = upstreamErrorMapper;
  }

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

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorDTO> handleConflictException(
    ConflictException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.CONFLICT, ErrorDTO.CategoryEnum.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.NOT_FOUND, CategoryEnum.NOT_FOUND);
  }

  @ExceptionHandler(InvalidOperatorRoleException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOperatorRoleException(InvalidOperatorRoleException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.GENERIC_ERROR);
  }

  @ExceptionHandler(InvalidDebtPositionException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDebtPositionException(InvalidDebtPositionException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler(ZipFileException.class)
  public ResponseEntity<ErrorDTO> handleZipFileException(ZipFileException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, CategoryEnum.GENERIC_ERROR);
  }

  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, ConversionFailedException.class, IllegalArgumentException.class})
  public ResponseEntity<ErrorDTO> handleViolationException(Exception ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.CategoryEnum.BAD_REQUEST);
  }

  @ExceptionHandler({AuthorizationDeniedException.class})
  public ResponseEntity<ErrorDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.FORBIDDEN, ErrorDTO.CategoryEnum.FORBIDDEN);
  }

  @ExceptionHandler({HttpClientErrorException.class})
  public ResponseEntity<ErrorDTO> handleHttpClientErrorException(HttpClientErrorException ex, HttpServletRequest request) {
    logException(ex, request, ex.getStatusCode());

    ErrorDTO.CategoryEnum category = transcodeStatus(ex.getStatusCode());
    String traceId = Utilities.getTraceId();
    String message = ex.getMessage();
    String code = "GENERIC_ERROR";

    UpstreamErrorMapper.MappedUpstreamError mapped = upstreamErrorMapper.from(ex);
    if(mapped != null) {
      message = mapped.description();
      code = mapped.code();
    }

    ErrorDTO dto = new ErrorDTO();
    dto.setCategory(category);
    dto.setMessage(message);
    dto.setTraceId(traceId);
    dto.setCode(code);

    return ResponseEntity
      .status(ex.getStatusCode())
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto);
  }

  private static CategoryEnum transcodeStatus(HttpStatusCode status) {
    if (status.isSameCodeAs(HttpStatus.NOT_FOUND)) return CategoryEnum.NOT_FOUND;
    if (status.isSameCodeAs(HttpStatus.CONFLICT)) return CategoryEnum.CONFLICT;
    if (status.isSameCodeAs(HttpStatus.FORBIDDEN)) return CategoryEnum.FORBIDDEN;
    if (status.is4xxClientError()) return CategoryEnum.BAD_REQUEST;
    return CategoryEnum.GENERIC_ERROR;
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

  @ExceptionHandler({ServletException.class, ErrorResponseException.class})
  public ResponseEntity<ErrorDTO> handleServletException(Exception ex, HttpServletRequest request) {
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorDTO.CategoryEnum category = ErrorDTO.CategoryEnum.GENERIC_ERROR;
    if (ex instanceof ErrorResponse errorResponse) {
      httpStatus = errorResponse.getStatusCode();
      if (httpStatus.isSameCodeAs(HttpStatus.NOT_FOUND)) {
        category = ErrorDTO.CategoryEnum.NOT_FOUND;
      } else if (httpStatus.is4xxClientError()) {
        category = ErrorDTO.CategoryEnum.BAD_REQUEST;
      }
    }
    return handleException(ex, request, httpStatus, category);
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ErrorDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.CategoryEnum.GENERIC_ERROR);
  }

  static ResponseEntity<ErrorDTO> handleException(Exception ex, HttpServletRequest request, HttpStatusCode httpStatus, ErrorDTO.CategoryEnum category) {
    logException(ex, request, httpStatus);

    String message = buildReturnedMessage(ex);
    String code;

    if (ex instanceof BaseBusinessException codedEx && StringUtils.isNotBlank(codedEx.getCode())) {
      code = codedEx.getCode();
    } else {
      ErrorMessageParser.ParsedError parsed = ErrorMessageParser.parse(message);
      code = parsed.code();
      if (parsed.description() != null) {
        message = parsed.description();
      }
    }

    ErrorDTO dto = new ErrorDTO();
    dto.setCategory(category);
    dto.setMessage(message);
    dto.setTraceId(Utilities.getTraceId());
    dto.setCode(code);

    return ResponseEntity
      .status(httpStatus)
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto);
  }

  private static void logException(Exception ex, HttpServletRequest request, HttpStatusCode httpStatus) {
    boolean printStackTrace = httpStatus.is5xxServerError();
    Level logLevel = printStackTrace ? Level.ERROR : Level.INFO;
    log.makeLoggingEventBuilder(logLevel)
      .log("A {} occurred handling request {}: HttpStatus {} - {}",
        ex.getClass(),
        getRequestDetails(request),
        httpStatus.value(),
        ex.getMessage(),
        printStackTrace ? ex : null
      );
    if (!printStackTrace && log.isDebugEnabled() && ex.getCause() != null) {
      log.debug("CausedBy: ", ex.getCause());
    }
  }

  private static String buildReturnedMessage(Exception ex) {
    switch (ex) {
      case HttpMessageNotReadableException httpMessageNotReadableException -> {
        if (httpMessageNotReadableException.getCause() instanceof DatabindException jsonMappingException) {
          return "Cannot parse body. " +
            jsonMappingException.getPath().stream()
              .map(JacksonException.Reference::getPropertyName)
              .collect(Collectors.joining(".")) +
            ": " + jsonMappingException.getOriginalMessage();
        }
        return "Required request body is missing";
      }
      case MethodArgumentNotValidException methodArgumentNotValidException -> {
        return "Invalid request content." +
          methodArgumentNotValidException.getBindingResult()
            .getAllErrors().stream()
            .map(e -> " " +
              (e instanceof FieldError fieldError ? fieldError.getField() : e.getObjectName()) +
              ": " + e.getDefaultMessage())
            .sorted()
            .collect(Collectors.joining(";"));
      }
      case ConstraintViolationException constraintViolationException -> {
        return "Invalid request content." +
          constraintViolationException.getConstraintViolations()
            .stream()
            .map(e -> " " + e.getPropertyPath() + ": " + e.getMessage())
            .sorted()
            .collect(Collectors.joining(";"));
      }
      default -> {
        return ex.getMessage();
      }
    }
  }

  static String getRequestDetails(HttpServletRequest request) {
    return "%s %s".formatted(request.getMethod(), request.getRequestURI());
  }
}

