package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenDTOMapper {

  public AccessTokenDTO toDTO(AccessToken accessToken) {
    AccessTokenDTO dto = new AccessTokenDTO();
    dto.setAccessToken(accessToken.getAccessToken());
    dto.setExpiresIn(accessToken.getExpiresIn());
    dto.setTokenType(accessToken.getTokenType());
    return dto;
  }

}
