package it.gov.pagopa.pu.bff.service.taxonomy;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import java.util.List;

public interface TaxonomyService {

  List<TaxonomyCollectionReasonDTO> getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode,String accessToken);

  List<TaxonomyMacroAreaCodeDTO> getMacroArea(String organizationType,String accessToken);

  List<TaxonomyOrganizationTypeDTO> getOrganizationTypes(String accessToken);

  List<TaxonomyServiceTypeCodeDTO> getServiceType(String organizationType, String macroAreaCode,String accessToken);

  List<TaxonomyCodeDTO> getTaxonomyCode(String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,String accessToken);
}
