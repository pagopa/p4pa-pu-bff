package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;

public interface OrgSubUnitOperatorsService {

  PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, Pageable pageable, String accessToken);
}
