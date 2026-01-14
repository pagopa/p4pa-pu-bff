package it.gov.pagopa.pu.bff.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.UpstreamErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO;
import it.gov.pagopa.pu.bff.dto.generated.ErrorDTO.TitleEnum;
import it.gov.pagopa.pu.bff.util.ErrorMessageParser;
import it.gov.pagopa.pu.bff.util.Utilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
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

  private final ObjectMapper objectMapper;

  public GlobalExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(InvalidAssessmentsRegistryException.class)
  public ResponseEntity<ErrorDTO> handleInvalidAssessmentRegistryException(InvalidAssessmentsRegistryException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidOrganizationException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOrganizationException(InvalidOrganizationException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDebtPositionTypeOrgException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDebtPositionTypeOrgException(InvalidDebtPositionTypeOrgException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorDTO> handleConflictException(
    ConflictException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.CONFLICT, ErrorDTO.TitleEnum.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.NOT_FOUND, TitleEnum.NOT_FOUND);
  }

  @ExceptionHandler(InvalidOperatorRoleException.class)
  public ResponseEntity<ErrorDTO> handleInvalidOperatorRoleException(InvalidOperatorRoleException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.GENERIC_ERROR);
  }

  @ExceptionHandler(InvalidDebtPositionException.class)
  public ResponseEntity<ErrorDTO> handleInvalidDebtPositionException(InvalidDebtPositionException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler(ZipFileException.class)
  public ResponseEntity<ErrorDTO> handleZipFileException(ZipFileException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, TitleEnum.GENERIC_ERROR);
  }

  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
  public ResponseEntity<ErrorDTO> handleViolationException(Exception ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ErrorDTO.TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler({AuthorizationDeniedException.class})
  public ResponseEntity<ErrorDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.FORBIDDEN, ErrorDTO.TitleEnum.FORBIDDEN);
  }

  @ExceptionHandler({HttpClientErrorException.class})
  public ResponseEntity<ErrorDTO> handleHttpClientErrorException(HttpClientErrorException ex, HttpServletRequest request) {
    logException(ex, request, ex.getStatusCode());

    // 1) Try to read the upstream error from body
    UpstreamErrorDTO upstream = tryParseUpstreamError(ex);

    String upstreamMessage = upstream != null ? upstream.getMessage() : null;
    String upstreamTraceId = upstream != null ? upstream.getTraceId() : null;

    // 2) Extract code and description from message
    ErrorMessageParser.ParsedError parsed = ErrorMessageParser.parse(upstreamMessage);

    // 3) Map title from status
    ErrorDTO.TitleEnum title = TitleEnum.GENERIC_ERROR;
    if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) title = TitleEnum.NOT_FOUND;
    else if (ex.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) title = TitleEnum.CONFLICT;
    else if (ex.getStatusCode().is4xxClientError()) title = TitleEnum.BAD_REQUEST;
    else if (ex.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) title = TitleEnum.FORBIDDEN;

    // 4) Resolve description independently
    String description = parsed.description();
    if (description == null) {
      description = upstreamMessage != null ? upstreamMessage : ex.getMessage();
    }

    ErrorDTO dto = new ErrorDTO();
    dto.setTitle(title);
    dto.setCode(parsed.code());
    dto.setDescription(description);
    dto.setTraceId(upstreamTraceId != null ? upstreamTraceId : Utilities.getTraceId());

    return ResponseEntity
      .status(ex.getStatusCode())
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto);
  }

  private UpstreamErrorDTO tryParseUpstreamError(HttpClientErrorException ex) {
    try {
      if (ex.getResponseBodyAsString().isBlank()) {
        return null;
      }
      return objectMapper.readValue(ex.getResponseBodyAsString(), UpstreamErrorDTO.class);
    } catch (Exception e) {
      return null;
    }
  }

  @ExceptionHandler(InvalidOrgSilServiceException.class)
  public  ResponseEntity<ErrorDTO> handleInvalidOrgSilServiceException(InvalidOrgSilServiceException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.BAD_REQUEST, TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidAssessmentsDetailException.class})
  public ResponseEntity<ErrorDTO> handleInvalidAssessmentsDetailException(InvalidAssessmentsDetailException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, TitleEnum.BAD_REQUEST);
  }

  @ExceptionHandler({ServletException.class, ErrorResponseException.class})
  public ResponseEntity<ErrorDTO> handleServletException(Exception ex, HttpServletRequest request) {
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorDTO.TitleEnum errorCode = ErrorDTO.TitleEnum.GENERIC_ERROR;
    if (ex instanceof ErrorResponse errorResponse) {
      httpStatus = errorResponse.getStatusCode();
      if (httpStatus.isSameCodeAs(HttpStatus.NOT_FOUND)) {
        errorCode = ErrorDTO.TitleEnum.NOT_FOUND;
      } else if (httpStatus.is4xxClientError()) {
        errorCode = ErrorDTO.TitleEnum.BAD_REQUEST;
      }
    }
    return handleException(ex, request, httpStatus, errorCode);
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ErrorDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ErrorDTO.TitleEnum.GENERIC_ERROR);
  }

  static ResponseEntity<ErrorDTO> handleException(Exception ex, HttpServletRequest request, HttpStatusCode httpStatus, ErrorDTO.TitleEnum errorEnum) {
    logException(ex, request, httpStatus);

    String message = buildReturnedMessage(ex);
    String description = message;
    String code;

    if (ex instanceof HasErrorCode codedEx && codedEx.getCode() != null && !codedEx.getCode().isBlank()) {
      code = codedEx.getCode();
    } else {
      ErrorMessageParser.ParsedError parsed = ErrorMessageParser.parse(message);
      code = parsed.code();
      if (parsed.description() != null) {
        description = parsed.description();
      }
    }

    ErrorDTO dto = new ErrorDTO();
    dto.setTitle(errorEnum);
    dto.setDescription(description);
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

