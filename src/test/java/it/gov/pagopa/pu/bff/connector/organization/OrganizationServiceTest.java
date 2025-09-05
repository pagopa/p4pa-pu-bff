package it.gov.pagopa.pu.bff.connector.organization;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationApiClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
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
  private OrganizationSearchClient organizationSearchClientMock;
  @Mock
  private OrganizationEntityClient organizationEntityClientMock;
  @Mock
  private OrganizationApiClient organizationApiClientMock;

  private OrganizationService service;

  @BeforeEach
  void setUp() {
    service = new OrganizationServiceImpl(organizationSearchClientMock, organizationEntityClientMock, organizationApiClientMock);
  }

  @Test
  void testGetOrganizationByIpaCode() {
    Organization expected = new Organization();
    String ipaCode = "ipaCode";
    String accessToken = "accessToken";

    when(organizationSearchClientMock.getOrganizationByIpaCode(Mockito.same(ipaCode), Mockito.same(accessToken)))
      .thenReturn(expected);

    Organization result = service.getOrganizationByIpaCode(ipaCode, accessToken);

    assertSame(expected, result);
  }

  @Test
  void testGetOrganizationByBrokerIdAndOrgName() {
    PagedModelOrganization expected = new PagedModelOrganization();
    Long brokerId = 1L;
    String orgName = "orgName";
    String accessToken = "accessToken";

    when(organizationSearchClientMock.getOrganizationByBrokerIdAndOrgName(Mockito.same(brokerId), Mockito.same(orgName), Mockito.any(), Mockito.same(accessToken)))
      .thenReturn(expected);

    PagedModelOrganization result = service.getOrganizationByBrokerIdAndOrgName(brokerId, orgName, Pageable.unpaged(), accessToken);

    assertSame(expected, result);
  }

  @Test
  void testGetOrganizationByOrganizationId() {
    Organization expected = new Organization();
    Long organizationId = 1L;
    String accessToken = "accessToken";

    when(organizationEntityClientMock.getOrganizationByOrganizationId(Mockito.same(organizationId), Mockito.same(accessToken)))
      .thenReturn(expected);

    Organization result = service.getOrganizationByOrganizationId(organizationId, accessToken);

    assertSame(expected, result);
  }

  @Test
  void  givenBrokerIdWhenGetOrganizationsByBrokerIdThenReturnPagedModelOrganization(){
    //given
    String accessToken = "ACCESSTOKEN";
    Long brokerId = 1L;
    PagedModelOrganization expectedResult = new PagedModelOrganization();

    when(organizationSearchClientMock.getOrganizationsByBrokerId(eq(brokerId), any(), eq(accessToken)))
      .thenReturn(expectedResult);

    //when
    PagedModelOrganization result = service.getOrganizationsByBrokerId(brokerId, Pageable.ofSize(1), accessToken);

    //then
    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void  givenOrganizationIdWhenGetOrganizationDetailThenReturnOrganizationDetailDTO(){
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    OrganizationDetailDTO expectedResult = new OrganizationDetailDTO();

    when(organizationApiClientMock.getOrganizationDetail(organizationId, accessToken))
      .thenReturn(expectedResult);

    OrganizationDetailDTO result = service.getOrganizationDetail(organizationId, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }
}
