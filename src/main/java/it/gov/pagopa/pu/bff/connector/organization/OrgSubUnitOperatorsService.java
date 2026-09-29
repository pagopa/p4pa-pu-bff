package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface OrgSubUnitOperatorsService {

  PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(Long organizationId, String subUnitCode, Set<String> mappedExternalUserIds, Pageable pageable, String accessToken);

  void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, String accessToken);

  void deleteOrgSubUnitFromOperator(Long organizationId, String mappedExternalUserId, String subUnitCode, String accessToken);

  void addOperatorsToOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, String accessToken);

  void deleteOperatorsFromOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, String accessToken);
}
