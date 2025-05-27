package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelTaxonomy;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.data.domain.Pageable;

public interface TaxonomyService {
  Taxonomy getTaxonomyDetail(Long taxonomyId, String accessToken);

  Taxonomy getTaxonomyByTaxonomyCode(String taxonomyCode, String accessToken);

  CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode, String accessToken);

  CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(String organizationType, String accessToken);

  CollectionModelTaxonomyOrganizationTypeDTO getOrganizationType(String accessToken);

  CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(String organizationType, String macroAreaCode, String accessToken);

  CollectionModelTaxonomyCodeDTO getTaxonomyCode(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, String accessToken);

  PagedModelTaxonomy getTaxonomies(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, Pageable pageable, String accessToken);

  WorkflowCreatedDTO synchronizeTaxonomy(String accessToken);
}
