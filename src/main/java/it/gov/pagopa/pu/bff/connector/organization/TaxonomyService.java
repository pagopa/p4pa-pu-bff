package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.*;

public interface TaxonomyService {

  CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode, String accessToken);

  CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(String organizationType, String accessToken);

  CollectionModelTaxonomyOrganizationTypeDTO getOrganizationType(String accessToken);

  CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(String organizationType, String macroAreaCode, String accessToken);

  CollectionModelTaxonomyCodeDTO getTaxonomyCode(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, String accessToken);
}
