package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AuthenticationApi;
import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
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
  public ResponseEntity<AccessTokenDTO> postToken(String idToken) {
    log.info("User requested postToken()");

    return new ResponseEntity<>(authorizationService.postToken(idToken), HttpStatus.OK);
  }

}
