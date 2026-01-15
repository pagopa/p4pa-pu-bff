package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrganizationSearchClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApiMock;

  private OrganizationSearchClient organizationSearchClient;

  @BeforeEach
  void setUp() {
    organizationSearchClient = new OrganizationSearchClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder,
      organizationSearchControllerApiMock
    );
  }

  @Test
  void whenGetOrganizationByIpaCodeThenInvokeWithAccessToken() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";
    Organization expectedResult = new Organization();

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(orgIpaCode))
      .thenReturn(expectedResult);

    // When
    Organization result = organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIpaCodeWhenGetOrganizationByIpaCodeThenNull() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(orgIpaCode))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When
    Organization result = organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken);

    // Then
    Assertions.assertNull(result);
  }


  @Test
  void whenGetOrganizationByBrokerIdAndOrgNameThenInvokeWithAccessToken() {
    // Given
    Long brokerId = 1L;
    String orgName = "ORGNAME";
    String accessToken = "ACCESSTOKEN";
    PagedModelOrganization expectedResult = new PagedModelOrganization();

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByBrokerIdAndOrgName(eq(String.valueOf(brokerId)), eq(orgName), any(), any(), anyList()))
      .thenReturn(expectedResult);

    // When
    PagedModelOrganization result = organizationSearchClient.getOrganizationByBrokerIdAndOrgName(brokerId, orgName,
      Pageable.unpaged(), accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenGetOrganizationsByBrokerIdAndFiltersThenInvokeWithAccessToken() {
    //given
    Long brokerId = 1L;
    String orgName = "orgName";
    String ipaCode = "ipaCode";
    String orgFiscalCode = "orgFiscalCode";
    String accessToken = "ACCESSTOKEN";
    Set<Long> allowedOrganizationIds = Set.of(123L);
    PagedModelOrganization expectedResult = new PagedModelOrganization();

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByBrokerIdAndFilters(brokerId, orgName, ipaCode, orgFiscalCode, allowedOrganizationIds, 0, 1, Collections.emptyList()))
      .thenReturn(expectedResult);

    //when
    PagedModelOrganization result = organizationSearchClient.getOrganizationsByBrokerIdAndFilters(brokerId, orgName, ipaCode, orgFiscalCode, allowedOrganizationIds, Pageable.ofSize(1), accessToken);

    //then
    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedResult, result);
  }
}
