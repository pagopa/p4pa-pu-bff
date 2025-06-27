package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTO;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrgSilServiceRetrieverServiceImpl implements OrgSilServiceRetrieverService {

  private final OrgSilServiceService orgSilServiceService;
  private final OrgSilServiceDTOMapper orgSilServiceDTOMapper;

  public OrgSilServiceRetrieverServiceImpl(OrgSilServiceService orgSilServiceService, OrgSilServiceDTOMapper orgSilServiceDTOMapper) {
      this.orgSilServiceService = orgSilServiceService;
      this.orgSilServiceDTOMapper = orgSilServiceDTOMapper;
  }

  @Override
  public List<OrgSilServiceDTO> getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId,loggedUser);
    CollectionModelOrgSilService collection = orgSilServiceService.getOrgSilServices(organizationId,serviceType,accessToken);
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

}
