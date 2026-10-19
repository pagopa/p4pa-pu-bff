package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndClientClient;
import it.gov.pagopa.pu.organization.dto.generated.PagedPdndClientView;
import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndClientServiceImpl implements PdndClientService {

  private final PdndClientClient client;

  public PdndClientServiceImpl(PdndClientClient client) {
    this.client = client;
  }

  @Override
  public List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, String accessToken) {
    return client.getPdndClientsByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, accessToken);
  }

  @Override
  public PdndClientNoSecretDTO getPdndClient(Long organizationId, String clientId, String accessToken) {
    return client.getPdndClient(organizationId, clientId, accessToken);
  }

  @Override
  public PdndClient savePdndClient(PdndClientDTO pdndClientDTO, String accessToken) {
    return client.savePdndClient(pdndClientDTO, accessToken);
  }

  @Override
  public void deletePdndClient(Long organizationId, String clientId, String accessToken) {
    client.deletePdndClient(organizationId, clientId, accessToken);
  }

  @Override
  public PagedPdndClientView getOrgSubUnitsPdndClients(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable, String accessToken) {
    return client.getOrgSubUnitsPdndClients(organizationId, subUnitCode, subUnitName, pageable, accessToken);
  }
}
