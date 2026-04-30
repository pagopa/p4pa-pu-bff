package it.gov.pagopa.pu.bff.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserInfoLimitedScope;
import it.gov.pagopa.pu.bff.controller.generated.AuthenticationApi;
import it.gov.pagopa.pu.bff.dto.generated.UserInfoDTO;
import it.gov.pagopa.pu.bff.mapper.UserInfoDTOMapper;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthenticationController implements AuthenticationApi {

  private final AuthorizationService authorizationService;
  private final UserInfoDTOMapper userInfoDTOMapper;

  public AuthenticationController(AuthorizationService authorizationService, UserInfoDTOMapper userInfoDTOMapper) {
    this.authorizationService = authorizationService;
    this.userInfoDTOMapper = userInfoDTOMapper;
  }

  @Override
  @SecurityRequirements // no security is required
  public ResponseEntity<AccessToken> postToken(String idToken) {
    log.info("User requested postToken()");

    return new ResponseEntity<>(authorizationService.postToken(idToken), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserInfoDTO> getUserInfo() {
    log.info("User requested getUserInfo");
    UserInfo userInfo = SecurityUtils.getLoggedUser();

    if (userInfo instanceof UserInfoLimitedScope) {
      throw new AuthorizationDeniedException("Limited scope UserInfo is not allowed");
    }

    return ResponseEntity.ok(userInfoDTOMapper.mapToDTO(userInfo));
  }

  @Override
  public ResponseEntity<Void> logout() {
    log.info("User requested logout");
    authorizationService.logout(SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }
}
