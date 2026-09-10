package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrgSubUnitOperatorsService {

  PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, Pageable pageable, String accessToken);

  void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, String accessToken);
}
