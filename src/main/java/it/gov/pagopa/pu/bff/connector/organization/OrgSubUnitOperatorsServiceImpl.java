package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitOperatorsClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitOperatorsSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgSubUnitOperatorsServiceImpl implements OrgSubUnitOperatorsService {

  private final OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClient;
  private final OrgSubUnitOperatorsClient orgSubUnitOperatorsClient;

  public OrgSubUnitOperatorsServiceImpl(OrgSubUnitOperatorsSearchClient orgSubUnitOperatorsSearchClient, OrgSubUnitOperatorsClient orgSubUnitOperatorsClient) {
    this.orgSubUnitOperatorsSearchClient = orgSubUnitOperatorsSearchClient;
    this.orgSubUnitOperatorsClient = orgSubUnitOperatorsClient;
  }

  @Override
  public PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, Pageable pageable, String accessToken) {
    return orgSubUnitOperatorsSearchClient.findByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, pageable, accessToken);
  }

  @Override
  public  void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, String accessToken) {
    orgSubUnitOperatorsClient.addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, accessToken);
  }
}
