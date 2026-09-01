package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitOperatorsSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitOperatorsServiceImpl implements OrgSubUnitOperatorsService {

  private final OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClient;

  public OrgSubUnitOperatorsServiceImpl(OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClient) {
    this.orgSubUnitOperatorsSearchClient = orgSubUnitOperatorsSearchClient;
  }

  @Override
  public PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, Pageable pageable, String accessToken) {
    return orgSubUnitOperatorsSearchClient.findByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, pageable, accessToken);
  }
}
