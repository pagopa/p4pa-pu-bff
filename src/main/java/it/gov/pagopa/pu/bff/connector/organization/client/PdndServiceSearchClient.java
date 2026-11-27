package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelPdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PdndServiceSearchClient {
  private final OrganizationApisHolder organizationApisHolder;

  public PdndServiceSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public List<PdndService> findByOrganizationIdAndClientId(Long organizationId, String clientId, PdndServiceType serviceType, String accessToken) {
    CollectionModelPdndService collectionModelPdndService = organizationApisHolder.getPdndServiceSearchControllerApi(accessToken)
      .crudPdndServicesFindByOrganizationIdAndClientId(organizationId, clientId, serviceType);
    return collectionModelPdndService != null && collectionModelPdndService.getEmbedded()!=null?collectionModelPdndService.getEmbedded().getPdndServices(): Collections.emptyList();
  }
}
