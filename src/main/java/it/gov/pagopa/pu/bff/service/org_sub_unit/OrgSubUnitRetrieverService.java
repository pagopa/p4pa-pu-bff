package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;

public interface OrgSubUnitRetrieverService {
  OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken);
  OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void deleteOrgSubUnit(Long organizationId, String orgSubUnitId, UserInfo loggedUser,String accessToken);
  OrgSubUnit updateOrgSubUnit(Long organizationId, String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
}
