package it.gov.pagopa.pu.bff.service.taxonomy;

import it.gov.pagopa.pu.bff.connector.organization.TaxonomyService;
import it.gov.pagopa.pu.bff.connector.workflow_hub.WorkflowTaxonomyService;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.mapper.TaxonomyMapper;
import it.gov.pagopa.pu.bff.mapper.taxonomy.*;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxonomyRetrieverServiceImpl implements TaxonomyRetrieverService {

  private final TaxonomyService taxonomyService;
  private final TaxonomyMapper taxonomyMapper;
  private final TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper;
  private final TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper;
  private final TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper;
  private final TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper;
  private final TaxonomyCodeMapper taxonomyCodeMapper;
  private final WorkflowTaxonomyService workflowTaxonomyService;

  public TaxonomyRetrieverServiceImpl(TaxonomyService taxonomyService,
                                      TaxonomyMapper taxonomyMapper,
                                      TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapper,
                                      TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapper,
                                      TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapper,
                                      TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapper,
                                      TaxonomyCodeMapper taxonomyCodeMapper,
                                      WorkflowTaxonomyService workflowTaxonomyService) {
    this.taxonomyService = taxonomyService;
    this.taxonomyMapper = taxonomyMapper;
    this.taxonomyOrganizationTypeMapper = taxonomyOrganizationTypeMapper;
    this.taxonomyMacroAreaCodeMapper = taxonomyMacroAreaCodeMapper;
    this.taxonomyCollectionReasonMapper = taxonomyCollectionReasonMapper;
    this.taxonomyServiceTypeCodeMapper = taxonomyServiceTypeCodeMapper;
    this.taxonomyCodeMapper = taxonomyCodeMapper;
    this.workflowTaxonomyService = workflowTaxonomyService;
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
  public List<TaxonomyCollectionReasonDTO> getCollectionReason(
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String accessToken) {
    return taxonomyService.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken)
      .getEmbedded().getTaxonomyCollectionReasonDTOes()
      .stream()
      .map(taxonomyCollectionReasonMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyMacroAreaCodeDTO> getMacroArea(
    String organizationType,
    String accessToken) {
    return taxonomyService.getMacroArea(organizationType, accessToken).getEmbedded()
      .getTaxonomyMacroAreaCodeDTOes()
      .stream()
      .map(taxonomyMacroAreaCodeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyOrganizationTypeDTO> getOrganizationTypes(
    String accessToken) {
    return taxonomyService.getOrganizationType(accessToken).getEmbedded()
      .getTaxonomyOrganizationTypeDTOes()
      .stream()
      .map(taxonomyOrganizationTypeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyServiceTypeCodeDTO> getServiceType(
    String organizationType,
    String macroAreaCode, String accessToken) {
    return taxonomyService.getServiceType(organizationType, macroAreaCode, accessToken).getEmbedded()
      .getTaxonomyServiceTypeCodeDTOes()
      .stream()
      .map(taxonomyServiceTypeCodeMapper::map)
      .toList();
  }

  @Override
  public List<TaxonomyCodeDTO> getTaxonomyCode(
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,
    String accessToken) {
    return taxonomyService.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken).getEmbedded()
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

  @Override
  public WorkflowCreatedDTO synchronizeTaxonomy(String accessToken) {
    return workflowTaxonomyService.synchronizeTaxonomy(accessToken);
  }
}
