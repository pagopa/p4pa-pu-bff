package it.gov.pagopa.pu.bff.service.taxonomy;

import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaxonomyRetrieverService {
  Taxonomy getTaxonomyDetail(Long taxonomyId, String accessToken);

  Taxonomy getTaxonomyByTaxonomyCode(String taxonomyCode, String accessToken);

  List<TaxonomyCollectionReasonDTO> getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode, String accessToken);

  List<TaxonomyMacroAreaCodeDTO> getMacroArea(String organizationType, String accessToken);

  List<TaxonomyOrganizationTypeDTO> getOrganizationTypes(String accessToken);

  List<TaxonomyServiceTypeCodeDTO> getServiceType(String organizationType, String macroAreaCode, String accessToken);

  List<TaxonomyCodeDTO> getTaxonomyCode(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, String accessToken);

  PagedTaxonomy getTaxonomies(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, Pageable pageable, String accessToken);

  WorkflowCreatedDTO synchronizeTaxonomy(String accessToken);
}
