package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrgSilServiceRetrieverServiceImpl implements OrgSilServiceRetrieverService {

  private final OrgSilServiceService orgSilServiceService;
  private final OrgSilServiceDTOMapper orgSilServiceDTOMapper;
  private final AuthorizationService authorizationService;
  private final OrgSilServiceViewMapper orgSilServiceViewMapper;

  public OrgSilServiceRetrieverServiceImpl(OrgSilServiceService orgSilServiceService,
                                           OrgSilServiceDTOMapper orgSilServiceDTOMapper,
                                           AuthorizationService authorizationService,
                                           OrgSilServiceViewMapper orgSilServiceViewMapper) {
    this.orgSilServiceService = orgSilServiceService;
    this.orgSilServiceDTOMapper = orgSilServiceDTOMapper;
    this.authorizationService = authorizationService;
    this.orgSilServiceViewMapper = orgSilServiceViewMapper;
  }

  @Override
  public List<OrgSilServiceExtendedDTO> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    CollectionModelOrgSilService collection = orgSilServiceService.getOrgSilServices(organizationId, serviceType, accessToken);
    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return orgSilServiceDTOMapper.map(collection.getEmbedded().getOrgSilServices());
  }

  @Override
  public String getOrgSilServiceApplicationName(Long serviceId, String accessToken) {
    if (serviceId == null) {
      return null;
    }
    OrgSilService service = orgSilServiceService.getOrgSilServiceById(serviceId, accessToken);
    return service != null ? service.getApplicationName() : null;
  }

  @Override
  public PagedOrgSilServiceView getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return orgSilServiceViewMapper.map(
      orgSilServiceService.getOrgSilServicesByFilters(organizationId, applicationName, serviceType, flagLegacy, pageable, accessToken));
  }

  @Override
  public OrgSilServiceDecryptedDTO getOrgSilServiceDetails(Long organizationId, Long orgSilServiceId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    return orgSilServiceDTOMapper.map(orgSilServiceService.getOrgSilServiceByIdDecrypted(orgSilServiceId, accessToken));
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
