package it.gov.pagopa.pu.bff.service.pdnd_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;

import java.util.List;

public interface PdndServiceRetrieverService {
  PdndService createPdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, UserInfo userInfo, String accessToken);

  PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, UserInfo userInfo, String accessToken);

  List<PdndServiceDTO> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, UserInfo userInfo, String accessToken);


  void deletePdndService(Long organizationId, String purposeId, String subUnitCode, UserInfo userInfo, String accessToken);

  List<PdndService> getPdndClientServices(Long organizationId, String clientId, PdndServiceType serviceType, UserInfo userInfo, String accessToken);
}
