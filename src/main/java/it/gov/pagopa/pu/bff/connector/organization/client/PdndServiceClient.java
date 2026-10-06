package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndServiceClient {
  private final OrganizationApisHolder organizationApisHolder;

  public PdndServiceClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode);
  }

  public PdndServiceView getPdndService(Long organizationId, String purposeId, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .getPdndService(organizationId, purposeId);
  }

  public List<PdndServiceView> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .getPdndServices(organizationId, subUnitCode, pdndServiceType);
  }

  public void deletePdndService(Long organizationId, String purposeId, String accessToken) {
    organizationApisHolder.getPdndServiceApi(accessToken)
      .deletePdndService(organizationId, purposeId);
  }

  public PagedPdndServiceView getOrgSubUnitsPdndServices(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .getOrgSubUnitsPdndServices(organizationId, subUnitCode, subUnitName,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
