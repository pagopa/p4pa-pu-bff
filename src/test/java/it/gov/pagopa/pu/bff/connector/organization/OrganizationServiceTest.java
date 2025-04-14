package it.gov.pagopa.pu.bff.connector.organization;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

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

    when(client.getOrganizationByIpaCode(Mockito.same(ipaCode), Mockito.same(accessToken)))
      .thenReturn(expected);

    Organization result = service.getOrganizationByIpaCode(ipaCode, accessToken);

    assertSame(expected, result);
  }

  @Test
  void testGetOrganizationByBrokerIdAndOrgName() {
    PagedModelOrganization expected = new PagedModelOrganization();
    String brokerId = "brokerId";
    String orgName = "orgName";
    String accessToken = "accessToken";

    when(client.getOrganizationByBrokerIdAndOrgName(Mockito.same(brokerId), Mockito.same(orgName), Mockito.any(), Mockito.same(accessToken)))
      .thenReturn(expected);

    PagedModelOrganization result = service.getOrganizationByBrokerIdAndOrgName(brokerId, orgName, Pageable.unpaged(), accessToken);

    assertSame(expected, result);
  }
}
