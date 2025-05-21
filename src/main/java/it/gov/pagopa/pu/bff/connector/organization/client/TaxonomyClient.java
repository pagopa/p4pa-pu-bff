package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelTaxonomy;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaxonomyClient {
  private final OrganizationApisHolder organizationApisHolder;

  public TaxonomyClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public Taxonomy getTaxonomyDetail(Long taxonomyId, String accessToken) {
    return organizationApisHolder.getTaxonomy(accessToken)
      .crudGetTaxonomy(String.valueOf(taxonomyId));
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

}
