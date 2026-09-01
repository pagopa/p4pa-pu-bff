package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

public interface OrgSubUnitOperatorsRetrieverService {

  PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, Pageable pageable, UserInfo loggedUser, String accessToken);
}
