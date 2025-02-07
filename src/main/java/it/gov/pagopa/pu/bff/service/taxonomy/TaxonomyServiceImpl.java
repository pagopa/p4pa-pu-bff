package it.gov.pagopa.pu.bff.service.taxonomy;

import it.gov.pagopa.pu.bff.connector.organization.TaxonomyClientService;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyCodeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyCollectionReasonMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyMacroAreaCodeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyOrganizationTypeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyServiceTypeCodeMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyServiceImpl implements TaxonomyService{

  private final TaxonomyClientService taxonomyClientService;
  private final TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper;
  private final TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper;
  private final TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper;
  private final TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper;
  private final TaxonomyCodeMapper taxonomyCodeMapper;

  public TaxonomyServiceImpl(TaxonomyClientService taxonomyClientService,
    TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper,
    TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper,
    TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper,
    TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper,
    TaxonomyCodeMapper taxonomyCodeMapper){
    this.taxonomyClientService = taxonomyClientService;
    this.taxonomyOrganizationTypeMapper = taxonomyOrganizationTypeMapper;
    this.taxonomyMacroAreaCodeMapper = taxonomyMacroAreaCodeMapper;
    this.taxonomyCollectionReasonMapper = taxonomyCollectionReasonMapper;
    this.taxonomyServiceTypeCodeMapper = taxonomyServiceTypeCodeMapper;
    this.taxonomyCodeMapper = taxonomyCodeMapper;
  }

  @Override
  public List<TaxonomyCollectionReasonDTO> getCollectionReason (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String accessToken){
    return taxonomyClientService.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken)
      .getEmbedded().getTaxonomyCollectionReasonDTOes()
      .stream()
      .map(taxonomyCollectionReasonMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyMacroAreaCodeDTO> getMacroArea (
    String organizationType,
    String accessToken) {
    return taxonomyClientService.getMacroArea(organizationType,accessToken).getEmbedded()
      .getTaxonomyMacroAreaCodeDTOes()
      .stream()
      .map(taxonomyMacroAreaCodeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyOrganizationTypeDTO> getOrganizationTypes (
    String accessToken) {
    return taxonomyClientService.getOrganizationType(accessToken).getEmbedded()
      .getTaxonomyOrganizationTypeDTOes()
      .stream()
      .map(taxonomyOrganizationTypeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyServiceTypeCodeDTO> getServiceType (
    String organizationType,
    String macroAreaCode, String accessToken) {
    return taxonomyClientService.getServiceType(organizationType,macroAreaCode,accessToken).getEmbedded()
      .getTaxonomyServiceTypeCodeDTOes()
      .stream()
      .map(taxonomyServiceTypeCodeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyCodeDTO> getTaxonomyCode (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,
    String accessToken) {
    return taxonomyClientService.getTaxonomyCode(organizationType,macroAreaCode,serviceTypeCode,collectionReason,accessToken).getEmbedded()
      .getTaxonomyCodeDTOes()
      .stream()
      .map(taxonomyCodeMapper::map)
      .toList();
  }
}
