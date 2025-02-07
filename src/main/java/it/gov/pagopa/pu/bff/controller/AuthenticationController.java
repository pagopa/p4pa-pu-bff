package it.gov.pagopa.pu.bff.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.bff.controller.generated.AuthenticationApi;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthenticationController implements AuthenticationApi {

  private final AuthorizationService authorizationService;

  public AuthenticationController(AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  @Override
  @SecurityRequirements // no security is required
  public ResponseEntity<AccessToken> postToken(String idToken) {
    log.info("User requested postToken()");

    return new ResponseEntity<>(authorizationService.postToken(idToken), HttpStatus.OK);
  }
}
