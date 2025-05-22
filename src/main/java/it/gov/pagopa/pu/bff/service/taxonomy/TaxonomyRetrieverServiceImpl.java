package it.gov.pagopa.pu.bff.service.taxonomy;

import it.gov.pagopa.pu.bff.connector.organization.TaxonomyService;
import it.gov.pagopa.pu.bff.dto.generated.PagedTaxonomy;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.mapper.TaxonomyMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyCodeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyCollectionReasonMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyMacroAreaCodeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyOrganizationTypeMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.TaxonomyServiceTypeCodeMapper;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyRetrieverServiceImpl implements TaxonomyRetrieverService {

  private final TaxonomyService taxonomyService;
  private final TaxonomyMapper taxonomyMapper;
  private final TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper;
  private final TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper;
  private final TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper;
  private final TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper;
  private final TaxonomyCodeMapper taxonomyCodeMapper;

  public TaxonomyRetrieverServiceImpl(TaxonomyService taxonomyService,
    TaxonomyMapper taxonomyMapper,
                                      TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper,
                                      TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper,
                                      TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper,
                                      TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper,
                                      TaxonomyCodeMapper taxonomyCodeMapper){
    this.taxonomyService = taxonomyService;
    this.taxonomyMapper = taxonomyMapper;
    this.taxonomyOrganizationTypeMapper = taxonomyOrganizationTypeMapper;
    this.taxonomyMacroAreaCodeMapper = taxonomyMacroAreaCodeMapper;
    this.taxonomyCollectionReasonMapper = taxonomyCollectionReasonMapper;
    this.taxonomyServiceTypeCodeMapper = taxonomyServiceTypeCodeMapper;
    this.taxonomyCodeMapper = taxonomyCodeMapper;
  }

  @Override
  public Taxonomy getTaxonomyDetail(Long taxonomyId, String accessToken) {
    return taxonomyService.getTaxonomyDetail(taxonomyId, accessToken);
  }

  @Override
  public Taxonomy getTaxonomyByTaxonomyCode(String taxonomyCode, String accessToken) {
    return taxonomyService.getTaxonomyByTaxonomyCode(taxonomyCode, accessToken);
  }

  @Override
  public List<TaxonomyCollectionReasonDTO> getCollectionReason (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String accessToken){
    return taxonomyService.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken)
      .getEmbedded().getTaxonomyCollectionReasonDTOes()
      .stream()
      .map(taxonomyCollectionReasonMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyMacroAreaCodeDTO> getMacroArea (
    String organizationType,
    String accessToken) {
    return taxonomyService.getMacroArea(organizationType,accessToken).getEmbedded()
      .getTaxonomyMacroAreaCodeDTOes()
      .stream()
      .map(taxonomyMacroAreaCodeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyOrganizationTypeDTO> getOrganizationTypes (
    String accessToken) {
    return taxonomyService.getOrganizationType(accessToken).getEmbedded()
      .getTaxonomyOrganizationTypeDTOes()
      .stream()
      .map(taxonomyOrganizationTypeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyServiceTypeCodeDTO> getServiceType (
    String organizationType,
    String macroAreaCode, String accessToken) {
    return taxonomyService.getServiceType(organizationType,macroAreaCode,accessToken).getEmbedded()
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
    return taxonomyService.getTaxonomyCode(organizationType,macroAreaCode,serviceTypeCode,collectionReason,accessToken).getEmbedded()
      .getTaxonomyCodeDTOes()
      .stream()
      .map(taxonomyCodeMapper::map)
      .toList();
  }

  @Override
  public PagedTaxonomy getTaxonomies(String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,
    Pageable pageable, String accessToken) {
    return taxonomyMapper.mapToPagedTaxonomy(
      taxonomyService.getTaxonomies(organizationType, macroAreaCode, serviceTypeCode, collectionReason, pageable, accessToken)
    );
  }
}
