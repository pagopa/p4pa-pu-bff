package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;

import java.util.List;

public interface PdndClientService {

  List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, String accessToken);

  PdndClientNoSecretDTO getPdndClient(Long organizationId, String clientId, String accessToken);

  PdndClient savePdndClient(PdndClientDTO pdndClientDTO, String accessToken);

  void deletePdndClient(Long organizationId, String clientId, String accessToken);
}
