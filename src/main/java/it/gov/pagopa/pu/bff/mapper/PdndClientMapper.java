package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.stereotype.Component;

@Component
public class PdndClientMapper {

  public PdndClientNoSecretDTO mapToPdndClientNoSecretDTO(PdndClient pdndClient) {
    if (pdndClient == null) {
      return null;
    }

    return PdndClientNoSecretDTO.builder()
      .clientId(pdndClient.getClientId())
      .organizationId(pdndClient.getOrganizationId())
      .subUnitCode(pdndClient.getSubUnitCode())
      .clientName(pdndClient.getClientName())
      .kid(pdndClient.getKid())
      .publicKey(pdndClient.getPublicKey())
      .build();
  }
}
