package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;

public interface OrgSubUnitRetrieverService {
  OrgSubUnit getOrgSubUnitById(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken);
  OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void deleteOrgSubUnit(Long organizationId, String subUnitCode, UserInfo loggedUser,String accessToken);
  OrgSubUnit updateOrgSubUnit(Long organizationId, String subUnitCode, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, UserInfo loggedUser, String accessToken);
}
