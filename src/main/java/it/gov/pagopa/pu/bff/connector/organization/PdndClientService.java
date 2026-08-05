package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;

import java.util.List;

public interface PdndClientService {

  List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, String accessToken);
}
