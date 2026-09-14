package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgSubUnitRetrieverService {
  OrgSubUnit getOrgSubUnitById(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken);
  OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void deleteOrgSubUnit(Long organizationId, String subUnitCode, UserInfo loggedUser,String accessToken);
  OrgSubUnit updateOrgSubUnit(Long organizationId, String subUnitCode, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken);
  void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, UserInfo loggedUser, String accessToken);
  PagedOrgSubUnit getPagedOrgSubUnits(PagedOrgSubUnitFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken);
  List<OrgAndSubUnitDTO> getOrgSubUnitWithNoServiceType(Long organizationId, PdndServiceType pdndServiceType, UserInfo loggedUser, String accessToken);
}
