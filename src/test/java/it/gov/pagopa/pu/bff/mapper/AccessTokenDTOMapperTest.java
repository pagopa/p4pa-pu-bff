package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessTokenDTOMapperTest {

  private final AccessTokenDTOMapper mapper = new AccessTokenDTOMapper();

  @Test
  void givenAccessTokenWhenToDTOThenCorrectMapping() {
    AccessToken accessToken = new AccessToken();
    accessToken.setAccessToken("fake-access-token");
    accessToken.setExpiresIn(3600);
    accessToken.setTokenType("bearer");

    AccessTokenDTO accessTokenDTO = mapper.toDTO(accessToken);

    Assertions.assertEquals("fake-access-token", accessTokenDTO.getAccessToken());
    Assertions.assertEquals(3600, accessTokenDTO.getExpiresIn());
    Assertions.assertEquals("bearer", accessTokenDTO.getTokenType());
  }

}
