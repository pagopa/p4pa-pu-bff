package it.gov.pagopa.pu.bff.service.pdnd_client;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndClientService;
import it.gov.pagopa.pu.bff.exception.InvalidPdndClientException;
import it.gov.pagopa.pu.bff.mapper.PdndClientMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndClientRetrieverServiceImpl implements PdndClientRetrieverService {

  private final AuthorizationService authorizationService;
  private final PdndClientService pdndClientService;
  private final PdndClientMapper pdndClientMapper;

  public PdndClientRetrieverServiceImpl(
    AuthorizationService authorizationService,
    PdndClientService pdndClientService,
    PdndClientMapper pdndClientMapper) {
    this.authorizationService = authorizationService;
    this.pdndClientService = pdndClientService;
    this.pdndClientMapper = pdndClientMapper;
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

  @Override
  public PdndClientNoSecretDTO createPdndClient(Long organizationId, PdndClientDTO pdndClientDTO, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(pdndClientDTO.getOrganizationId(), userInfo);

    validateOrganizationForPdndClient(organizationId, pdndClientDTO.getOrganizationId());

    PdndClient pdndClient = pdndClientService.savePdndClient(pdndClientDTO, accessToken);

    return pdndClientMapper.mapToPdndClientNoSecretDTO(pdndClient);
  }

  private void validateOrganizationForPdndClient(Long organizationId, Long orgIdFromPdndClient) {
    if(!organizationId.equals(orgIdFromPdndClient)){
      throw new InvalidPdndClientException("INVALID_PDND_CLIENT",
        String.format("Mismatch organizationId %s retrieved from path request with organizationId %s retrieved from body request", organizationId , orgIdFromPdndClient));
    }
  }

  @Override
  public void deletePdndClient(Long organizationId, String clientId, UserInfo userInfo, String accessToken) {
    authorizationService.validateAdminRole(organizationId, userInfo);
    pdndClientService.deletePdndClient(organizationId, clientId, accessToken);
  }
}
