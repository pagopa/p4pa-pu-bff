package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.dto.UserInfoDTO;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@Slf4j
public class AuthorizationService {

  private final RestTemplate restTemplate;

  public AuthorizationService(@Value("${app.auth.base-url}") String authServerBaseUrl,
    RestTemplateBuilder restTemplateBuilder) {
    DefaultUriBuilderFactory ubf = new DefaultUriBuilderFactory(authServerBaseUrl);
    ubf.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
    this.restTemplate = restTemplateBuilder
      .uriTemplateHandler(ubf)
      .build();
  }

  public UserInfoDTO validateToken(String accessToken){
    log.info("Requesting validate token");
    try{
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
      return restTemplate.exchange("/auth/userinfo", HttpMethod.GET, new HttpEntity<>(headers), UserInfoDTO.class).getBody();
    } catch (HttpStatusCodeException ex){
      String errorMessage;
      if(HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())){
        errorMessage="Bad Access Token provided";
        log.info(errorMessage);
      } else {
        errorMessage="Something gone wrong while validate token";
        log.error(errorMessage, ex);
      }
      throw new InvalidAccessTokenException(errorMessage);
    }
  }
}
