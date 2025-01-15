package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class TaxonomyClient {
  private final OrganizationApisHolder organizationApisHolder;

  public TaxonomyClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(String organizationType,
    String macroAreaCode, String serviceTypeCode, String accessToken){
    try {
      return organizationApisHolder.getTaxonomyEntityControllerApi(accessToken).crudTaxonomiesCollectionReasonFindDistinctCollectionReasonByOrganizationTypeAndMacroAreaCodeAndServiceTypeCodeOrderByCollectionReasonAsc(organizationType,macroAreaCode,serviceTypeCode);
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving taxonomy collection reason", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving taxonomy collection reason", e);
      throw e;
    }
  }

  public CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(String organizationType,
    String accessToken){
    try {
      return organizationApisHolder.getMacroArea(accessToken).crudTaxonomiesMacroAreaFindDistinctMacroAreaCodeByOrganizationTypeOrderByMacroAreaCodeAsc(organizationType);
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving taxonomy macro area code", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving taxonomy macro area code", e);
      throw e;
    }
  }

  public CollectionModelTaxonomyOrganizationTypeDTO getOrganizationType(String accessToken){
    try {
      return organizationApisHolder.getOrganizationTypes(accessToken).crudTaxonomiesOrganizationTypesFindDistinctOrganizationTypeByOrderByOrganizationTypeAsc();
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving taxonomy organization type", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving taxonomy organization type", e);
      throw e;
    }
  }

  public CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(String organizationType,
    String macroAreaCode, String accessToken){
    try {
      return organizationApisHolder.getServiceType(accessToken).crudTaxonomiesServiceTypeFindDistinctServiceTypeCodeByOrganizationTypeAndMacroAreaCodeOrderByServiceTypeCodeAsc(organizationType,macroAreaCode);
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving taxonomy service type", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving taxonomy service type", e);
      throw e;
    }
  }

  public CollectionModelTaxonomyCodeDTO getTaxonomyCode(String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,
    String accessToken){
    try {
      return organizationApisHolder.getTaxonomyCode(accessToken).crudTaxonomiesTaxonomyCodeFindDistinctTaxonomyCodeByOrganizationTypeAndMacroAreaCodeAndServiceTypeCodeAndCollectionReasonOrderByTaxonomyCodeAsc(organizationType,macroAreaCode,serviceTypeCode,collectionReason);
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving taxonomy code", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving taxonomy code", e);
      throw e;
    }
  }

}
