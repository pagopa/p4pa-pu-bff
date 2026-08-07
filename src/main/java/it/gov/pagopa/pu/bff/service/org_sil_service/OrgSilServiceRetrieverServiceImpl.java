package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.exception.common.ConflictException;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrgSilServiceRetrieverServiceImpl implements OrgSilServiceRetrieverService {

  private final OrgSilServiceService orgSilServiceService;
  private final OrgSilServiceDTOMapper orgSilServiceDTOMapper;
  private final AuthorizationService authorizationService;
  private final OrgSilServiceViewMapper orgSilServiceViewMapper;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public OrgSilServiceRetrieverServiceImpl(OrgSilServiceService orgSilServiceService,
                                           OrgSilServiceDTOMapper orgSilServiceDTOMapper,
                                           AuthorizationService authorizationService,
                                           OrgSilServiceViewMapper orgSilServiceViewMapper,
                                           DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.orgSilServiceService = orgSilServiceService;
    this.orgSilServiceDTOMapper = orgSilServiceDTOMapper;
    this.authorizationService = authorizationService;
    this.orgSilServiceViewMapper = orgSilServiceViewMapper;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
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
  public void deleteOrgSilService(Long organizationId, Long orgSilServiceId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    Long usageCount = debtPositionTypeOrgService.countByOrgSilServiceId(orgSilServiceId, accessToken);
    if (usageCount > 0) {
      throw new ConflictException("ORG_SIL_SERVICE_IN_USE", "Cannot delete OrgSilService with ID " + orgSilServiceId + ": it is referenced by " + usageCount + " DebtPositionTypeOrg record(s).");
    }

    orgSilServiceService.deleteOrgSilService(orgSilServiceId, accessToken);
  }
}
