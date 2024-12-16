package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4paauth.controller.ApiClient;
import it.gov.pagopa.pu.p4paauth.controller.generated.AuthnApi;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Log4j2
@Service
public class AuthClientImpl implements AuthClient{
  private final AuthnApi authApi;
  public AuthClientImpl(RestTemplateBuilder restTemplateBuilder,
      @Value("${app.auth.base-url}") String baseUrl){
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(baseUrl);
    authApi = new AuthnApi(apiClient);
  }
  @Override
  public UserInfo validateToken (String accessToken) {
    authApi.getApiClient().setBearerToken(accessToken);
    return authApi.getUserInfo();
  }
}
