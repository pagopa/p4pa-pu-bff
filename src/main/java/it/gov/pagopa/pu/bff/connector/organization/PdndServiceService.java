package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;

public interface PdndServiceService {
  PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken);

  PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken);
}
