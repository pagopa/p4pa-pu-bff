package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrgSilServiceStorerServiceImpl implements OrgSilServiceStorerService {
  private final OrgSilServiceService orgSilServiceService;
  private final OrgSilServiceDTOMapper orgSilServiceDTOMapper;
  private final AuthorizationService authorizationService;

  public OrgSilServiceStorerServiceImpl(OrgSilServiceService orgSilServiceService,
                                        OrgSilServiceDTOMapper orgSilServiceDTOMapper,
                                        AuthorizationService authorizationService) {
    this.orgSilServiceService = orgSilServiceService;
    this.orgSilServiceDTOMapper = orgSilServiceDTOMapper;
    this.authorizationService = authorizationService;
  }

  @Override
  public OrgSilServiceDecryptedDTO createOrgSilService(Long organizationId, OrgSilServiceDecryptedDTO body, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    if (body.getOrgSilServiceId() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orgSilServiceId must not be provided when creating a new OrgSilService");
    }

    validateAuthConfig(body);

    OrgSilServiceDTO orgSilServiceDTO = orgSilServiceDTOMapper.toOrgSilServiceDTO(body);

    return orgSilServiceDTOMapper.map(
      orgSilServiceService.createOrUpdateOrgSilService(orgSilServiceDTO, accessToken));
  }

  private void validateAuthConfig(OrgSilServiceDecryptedDTO body) {
    boolean isLegacy = Boolean.TRUE.equals(body.getFlagLegacy());
    boolean hasBasicAuthConfig = body.getLegacyBasicAuthConfig() != null;
    boolean hasJwtAuthConfig = body.getLegacyJwtAuthConfig() != null;

    if (!isLegacy) {
      // flagLegacy = false ⇒ no auth config
      if (hasBasicAuthConfig || hasJwtAuthConfig) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "When flagLegacy is false, authConfig must not be provided.");
      }
    } else {
      // flagLegacy = true ⇒ exactly one auth config
      if (hasBasicAuthConfig == hasJwtAuthConfig) { // both true or both false
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "When flagLegacy is true, exactly one authConfig (legacyBasic or legacyJwt) must be provided.");
      }
    }
  }
}
