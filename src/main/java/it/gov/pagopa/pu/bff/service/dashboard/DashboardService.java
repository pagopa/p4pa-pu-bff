package it.gov.pagopa.pu.bff.service.dashboard;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByIuf;
import it.gov.pagopa.pu.bff.dto.generated.DashboardByIuv;

public interface DashboardService {
  DashboardByFc getDashboardByFiscalCode(Long organizationId, String fiscalCode, UserInfo loggedUser, String accessToken);
  DashboardByIuf getDashboardByIuf(Long organizationId, String iuf, UserInfo loggedUser, String accessToken);
  DashboardByIuv getDashboardByIuv(Long organizationId, String iuv, UserInfo loggedUser, String accessToken);
}
