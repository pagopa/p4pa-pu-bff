package it.gov.pagopa.pu.bff.service.analytics;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {
  private final String supersetSSOUrl;
  private final AuthorizationService authorizationService;

  public AnalyticsServiceImpl(@Value("${analytics.superset.sso-url}") String supersetSSOUrl, AuthorizationService authorizationService) {
    this.supersetSSOUrl = supersetSSOUrl;
    this.authorizationService = authorizationService;
  }

  @Override
  public String generateSupersetUrl(Long organizationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    AccessToken token = authorizationService.postLimitedToken(organizationId, "superset", accessToken);
    return supersetSSOUrl+"/?token="+token.getAccessToken();
  }
}
