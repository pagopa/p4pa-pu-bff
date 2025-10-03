package it.gov.pagopa.pu.bff.service.dashboard;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;

public interface DashboardService {
  DashboardByFc getDashboardByFiscalCode(Long organizationId, String fiscalCode, UserInfo loggedUser, String accessToken);

}
