package it.gov.pagopa.pu.bff.service.taxonomy;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;

public interface TaxonomyService {

  CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode,String accessToken);

  CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(String organizationType,String accessToken);

  CollectionModelTaxonomyOrganizationTypeDTO getOrganizationTypes(String accessToken);

  CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(String organizationType, String macroAreaCode,String accessToken);

  CollectionModelTaxonomyCodeDTO getTaxonomyCode(String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,String accessToken);
}
