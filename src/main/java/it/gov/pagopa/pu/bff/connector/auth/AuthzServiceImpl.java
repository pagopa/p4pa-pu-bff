package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthzServiceImpl implements AuthzService {

  private final AuthzClient client;

  public AuthzServiceImpl(AuthzClient client) {
    this.client = client;
  }

  @Override
  public UserInfo getUserInfoFromMappedExternaUserId(String mappedExternalUserId, String accessToken) {
    return client.getUserInfoFromMappedExternalUserId(mappedExternalUserId, accessToken);
  }

  @Override
  public OperatorsPage getOrganizationOperators(
    String organizationIpaCode, String fiscalCode, String firstName,
    String lastName, List<String> mappedExternalUserIdsToExclude,
    Pageable pageable, String accessToken) {
    return client.getOrganizationOperators(organizationIpaCode, fiscalCode,
      firstName, lastName, mappedExternalUserIdsToExclude, pageable, accessToken);
  }

  @Override
  public OperatorDTO getOrganizationOperator(String organizationIpaCode, String mappedExternalUserId, String accessToken) {
    return client.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);
  }

}
