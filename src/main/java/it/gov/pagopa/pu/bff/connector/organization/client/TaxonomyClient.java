package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class TaxonomyClient {
  private final OrganizationApisHolder organizationApisHolder;
  private final WorkflowHubApisHolder workflowHubApisHolder;

  public TaxonomyClient(OrganizationApisHolder organizationApisHolder, WorkflowHubApisHolder workflowHubApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
    this.workflowHubApisHolder = workflowHubApisHolder;
  }

  public Taxonomy getTaxonomyDetail(Long taxonomyId, String accessToken) {
    try {
      return organizationApisHolder.getTaxonomy(accessToken)
        .crudGetTaxonomy(String.valueOf(taxonomyId));
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Taxonomy with taxonomyId {} not found", taxonomyId);
      return null;
    }
  }

  public Taxonomy getTaxonomyByTaxonomyCode(String taxonomyCode, String accessToken) {
    return organizationApisHolder.getTaxonomySearchControllerApi(accessToken)
      .crudTaxonomiesFindByTaxonomyCode(taxonomyCode);
  }

  public CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(
    String organizationType, String macroAreaCode, String serviceTypeCode, String accessToken
  ) {
    return organizationApisHolder.getTaxonomyEntityControllerApi(accessToken).crudTaxonomiesCollectionReasonFindCollectionReasons(organizationType, macroAreaCode, serviceTypeCode);
  }

  public CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(
    String organizationType, String accessToken
  ) {
    return organizationApisHolder.getMacroArea(accessToken).crudTaxonomiesMacroAreaFindMacroAreaCodes(organizationType);
  }

  public CollectionModelTaxonomyOrganizationTypeDTO getOrganizationType(String accessToken) {
    return organizationApisHolder.getOrganizationTypes(accessToken).crudTaxonomiesOrganizationTypesFindOrganizationTypes();
  }

  public CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(
    String organizationType, String macroAreaCode, String accessToken
  ) {
    return organizationApisHolder.getServiceType(accessToken).crudTaxonomiesServiceTypeFindServiceTypeCodes(organizationType, macroAreaCode);
  }

  public CollectionModelTaxonomyCodeDTO getTaxonomyCode(
    String organizationType, String macroAreaCode, String serviceTypeCode,
    String collectionReason, String accessToken
  ) {
    return organizationApisHolder.getTaxonomyCode(accessToken).crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(organizationType, macroAreaCode, serviceTypeCode, collectionReason);
  }

  public PagedModelTaxonomy getTaxonomies(
    String organizationType, String macroAreaCode, String serviceTypeCode,
    String collectionReason, Pageable pageable, String accessToken
  ) {
    return organizationApisHolder.getTaxonomySearchControllerApi(accessToken)
      .crudTaxonomiesFindTaxonomies(organizationType, macroAreaCode, serviceTypeCode, collectionReason,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public WorkflowCreatedDTO synchronizeTaxonomy(String accessToken) {
    return workflowHubApisHolder.getTaxonomyApi(accessToken)
      .synchronizeTaxonomy();
  }

}
