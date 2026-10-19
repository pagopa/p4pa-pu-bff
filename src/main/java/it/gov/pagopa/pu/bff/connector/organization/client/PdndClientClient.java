package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedPdndClientView;
import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndClientClient {

  private final OrganizationApisHolder organizationApisHolder;

  public PdndClientClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, String accessToken) {
    return organizationApisHolder.getPdndClientApi(accessToken)
      .getPdndClientsByOrganizationIdAndSubUnitCode(organizationId, subUnitCode);
  }

  public PdndClientNoSecretDTO getPdndClient(Long organizationId, String clientId, String accessToken) {
    return organizationApisHolder.getPdndClientApi(accessToken)
      .getPdndClient(organizationId, clientId);
  }

  public PdndClient savePdndClient(PdndClientDTO pdndClientDTO, String accessToken) {
    return organizationApisHolder.getPdndClientApi(accessToken)
      .savePdndClient(pdndClientDTO);
  }

  public void deletePdndClient(Long organizationId, String clientId, String accessToken) {
    organizationApisHolder.getPdndClientApi(accessToken)
      .deletePdndClient(organizationId, clientId);
  }

  public PagedPdndClientView getOrgSubUnitsPdndClients(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable, String accessToken) {
    return organizationApisHolder.getPdndClientApi(accessToken)
      .getOrgSubUnitsPdndClients(organizationId, subUnitCode, subUnitName,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
