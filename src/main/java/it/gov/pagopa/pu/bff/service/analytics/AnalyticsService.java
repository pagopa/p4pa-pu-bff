package it.gov.pagopa.pu.bff.service.analytics;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public interface AnalyticsService {
  String generateSupersetUrl(Long organizationId, UserInfo loggedUser, String accessToken);
}
