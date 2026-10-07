package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OrgSubUnitNotRelatedOperatorsFilters;
import it.gov.pagopa.pu.bff.dto.OrgSubUnitOperatorsFilters;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgSubUnitOperatorsRetrieverService {

  PagedOrgSubUnitOperators getOrgSubUnitNotRelatedOperators(Long organizationId, String subUnitCode, OrgSubUnitNotRelatedOperatorsFilters filters, Pageable pageable, UserInfo loggedUser, String accessToken);

  PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, OrgSubUnitOperatorsFilters filters, Pageable pageable, UserInfo loggedUser, String accessToken);

  void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, UserInfo loggedUser, String accessToken);

  void deleteOrgSubUnitFromOperator(Long organizationId, String mappedExternalUserId, String subUnitCode, UserInfo loggedUser, String accessToken);

  void addOperatorsToOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, UserInfo loggedUser, String accessToken);

  void deleteOperatorsFromOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, UserInfo loggedUser, String accessToken);
}
