package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthzService {
  UserInfo getUserInfoFromMappedExternaUserId(String mappedExternalUserId, String accessToken);
  OperatorsPage getOrganizationOperators(String organizationIpaCode, String fiscalCode, String firstName, String lastName, List<String> mappedExternalUserIdsToExclude, Pageable pageable, String accessToken);
  OperatorDTO getOrganizationOperator(String organizationIpaCode, String mappedExternalUserId, String accessToken);
}
