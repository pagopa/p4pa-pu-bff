package it.gov.pagopa.pu.bff.service.pdnd_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PdndServiceRetrieverService {
  PdndService createPdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, UserInfo userInfo, String accessToken);

  PdndServiceView getPdndService(Long organizationId, String purposeId, UserInfo userInfo, String accessToken);

  List<PdndServiceView> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, UserInfo userInfo, String accessToken);


  void deletePdndService(Long organizationId, String purposeId, UserInfo userInfo, String accessToken);

  List<PdndService> getPdndClientServices(Long organizationId, String clientId, PdndServiceType serviceType, UserInfo userInfo, String accessToken);

  PagedPdndServiceView getOrgSubUnitsPdndServices(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable, UserInfo userInfo, String accessToken);
}
