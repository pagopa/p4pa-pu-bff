package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgSubUnitOperatorsRetrieverService {

  PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, Pageable pageable, UserInfo loggedUser, String accessToken);

  void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, UserInfo loggedUser, String accessToken);
}
