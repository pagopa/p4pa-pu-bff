package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;

import java.util.List;

public interface PdndServiceService {
  PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken);

  PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken);

  List<PdndServiceDTO> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, String accessToken);

  void deletePdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken);
}
