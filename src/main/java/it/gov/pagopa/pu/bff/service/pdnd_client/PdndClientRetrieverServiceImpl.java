package it.gov.pagopa.pu.bff.service.pdnd_client;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndClientService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndClientRetrieverServiceImpl implements PdndClientRetrieverService {

  private final AuthorizationService authorizationService;
  private final PdndClientService pdndClientService;

  public PdndClientRetrieverServiceImpl(AuthorizationService authorizationService, PdndClientService pdndClientService) {
    this.authorizationService = authorizationService;
    this.pdndClientService = pdndClientService;
  }

  @Override
  public List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndClientService.getPdndClientsByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, accessToken);
  }

  @Override
  public PdndClientNoSecretDTO getPdndClient(Long organizationId, String clientId, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    return pdndClientService.getPdndClient(organizationId, clientId, accessToken);
  }
}
