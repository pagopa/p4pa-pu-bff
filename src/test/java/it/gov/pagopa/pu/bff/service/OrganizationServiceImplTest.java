package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.service.organization.OrganizationServiceImpl;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.model.generated.UserOrganizationRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

  @Mock
  private OrganizationClientImpl organizationClientMock;
  private OrganizationServiceImpl organizationService;
  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private EntityModelOrganization entityModelOrganization;
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");
    userOrganizationRoles.setRoles(Collections.singletonList("Admin"));

    userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));

    entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setOrganizationId(Long.valueOf("123"));
    entityModelOrganization.setIpaCode("testIpaCode");
    entityModelOrganization.setOrgName("Test Organization");

    organizationService = new OrganizationServiceImpl(organizationClientMock);
  }

  @Test
  void testGetOrganizations() {
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(Long.valueOf("123"), result.get(0).getOrganizationId());
    assertEquals("testIpaCode", result.get(0).getIpaCode());
    assertEquals("Test Organization", result.get(0).getOrgName());
    assertEquals("Admin", result.get(0).getOperatorRole());
  }

  @Test
  void testGetOrganizations_EmptyList() {
    userInfo.setOrganizations(Collections.emptyList());

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(0, result.size());
  }

  @Test
  void testGetOrganizations_GivenNullOrganization() {
    userOrganizationRoles.setRoles(Collections.emptyList());
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(null);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(null, result.get(0).getOrganizationId());
    assertEquals(null, result.get(0).getIpaCode());
    assertEquals(null, result.get(0).getOrgName());
    assertEquals(null, result.get(0).getOperatorRole());
  }
}
