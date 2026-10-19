package it.gov.pagopa.pu.bff.service.pdnd_client;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.organization.dto.generated.PagedPdndClientView;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PdndClientRetrieverService {

  List<PdndClientNoSecretDTO> getPdndClientsByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, UserInfo userInfo, String accessToken);

  PdndClientNoSecretDTO getPdndClient(Long organizationId, String clientId, UserInfo userInfo, String accessToken);

  PdndClientNoSecretDTO savePdndClient(Long organizationId, PdndClientDTO pdndClientDTO, UserInfo userInfo, String accessToken);

  void deletePdndClient(Long organizationId, String clientId, UserInfo loggedUser, String accessToken);

  PagedPdndClientView getOrgSubUnitsPdndClients(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable, UserInfo loggedUser, String accessToken);
}
