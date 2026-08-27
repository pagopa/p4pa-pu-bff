package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;

public interface OrgSubUnitService {
  OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken);
  OrgSubUnit createOrgSubUnit(OrgSubUnitRequestBody orgSubUnit, String accessToken);
  void deleteOrgSubUnit(String orgSubUnitId, String accessToken);
  OrgSubUnit updateOrgSubUnit(String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, String accessToken);
  void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, String accessToken);
}
