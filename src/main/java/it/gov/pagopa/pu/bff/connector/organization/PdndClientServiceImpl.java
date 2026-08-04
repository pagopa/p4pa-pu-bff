package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndClientClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
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
}
