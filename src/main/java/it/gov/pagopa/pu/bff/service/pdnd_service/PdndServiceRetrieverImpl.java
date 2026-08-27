package it.gov.pagopa.pu.bff.service.pdnd_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndServiceService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class PdndServiceRetrieverImpl implements PdndServiceRetrieverService {

  private final AuthorizationService authorizationService;
  private final PdndServiceService pdndServiceService;

  public PdndServiceRetrieverImpl(AuthorizationService authorizationService, PdndServiceService pdndServiceService) {
    this.authorizationService = authorizationService;
    this.pdndServiceService = pdndServiceService;
  }

  @Override
  public PdndService createPdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndServiceService.savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode, accessToken);
  }
}
