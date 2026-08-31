package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;

public interface OrgSubUnitService {
  OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken);
  OrgSubUnit createOrgSubUnit(OrgSubUnitRequestBody orgSubUnit, String accessToken);
  void deleteOrgSubUnit(String orgSubUnitId, String accessToken);
  OrgSubUnit updateOrgSubUnit(String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, String accessToken);
  void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, String accessToken);
  PagedModelOrgSubUnit findByOrganizationIdAndFilters(Long organizationId, String operatorExternalUserId, String subUnitCode, OrgSubUnitStatus status, SubUnitType subUnitType, Pageable pageable, String accessToken);
}
