package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationRetrieverServiceTest {

  @Mock
  private OrganizationSearchClient client;

  private OrganizationService service;

  @BeforeEach
  void setUp() {
    service = new OrganizationServiceImpl(client);
  }

  @Test
  void testGetOrganizationByIpaCode() {
    Organization expected = new Organization();
    String ipaCode = "ipaCode";
    String accessToken = "accessToken";

    when(client.getOrganizationByIpaCode(ipaCode, accessToken)).thenReturn(expected);

    Organization result = service.getOrganizationByIpaCode(ipaCode, accessToken);

    assertEquals(expected, result);
  }
}
