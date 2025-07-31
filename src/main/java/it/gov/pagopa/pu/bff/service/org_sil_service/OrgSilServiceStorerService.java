package it.gov.pagopa.pu.bff.service.org_sil_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;

public interface OrgSilServiceStorerService {
  OrgSilServiceDecryptedDTO createOrgSilService(Long organizationId, OrgSilServiceDecryptedDTO body, UserInfo loggedUser, String accessToken);

  OrgSilServiceDecryptedDTO updateOrgSilService(Long organizationId, OrgSilServiceDecryptedDTO body, UserInfo loggedUser, String accessToken);
}
