package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import org.springframework.data.domain.Pageable;

public interface OrgSubUnitRetrieverService {
  OrgSubUnit getOrgSubUnitById(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken);
  OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void deleteOrgSubUnit(Long organizationId, String subUnitCode, UserInfo loggedUser,String accessToken);
  OrgSubUnit updateOrgSubUnit(Long organizationId, String subUnitCode, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, UserInfo loggedUser, String accessToken);
  PagedOrgSubUnit getPagedOrgSubUnits(PagedOrgSubUnitFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken);
}
