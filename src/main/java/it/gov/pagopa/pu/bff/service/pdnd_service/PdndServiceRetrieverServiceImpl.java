package it.gov.pagopa.pu.bff.service.pdnd_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndServiceService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndServiceRetrieverServiceImpl implements PdndServiceRetrieverService {

  private final AuthorizationService authorizationService;
  private final PdndServiceService pdndServiceService;

  public PdndServiceRetrieverServiceImpl(AuthorizationService authorizationService, PdndServiceService pdndServiceService) {
    this.authorizationService = authorizationService;
    this.pdndServiceService = pdndServiceService;
  }

  @Override
  public PdndService createPdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndServiceService.savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode, accessToken);
  }

  @Override
  public PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndServiceService.getPdndService(organizationId, purposeId, subUnitCode, accessToken);
  }

  @Override
  public List<PdndServiceDTO> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndServiceService.getPdndServices(organizationId, subUnitCode, pdndServiceType, accessToken);
  }

  @Override
  public void deletePdndService(Long organizationId, String purposeId, String subUnitCode, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    pdndServiceService.deletePdndService(organizationId, purposeId, subUnitCode, accessToken);
  }

  @Override
  public List<PdndService> getPdndClientServices(Long organizationId, String clientId, PdndServiceType serviceType, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndServiceService.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, accessToken);
  }
}
