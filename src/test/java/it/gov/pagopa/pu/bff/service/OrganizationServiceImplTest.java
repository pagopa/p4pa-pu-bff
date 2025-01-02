package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.service.organization.OrganizationServiceImpl;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserOrganizationRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

  @Mock
  private OrganizationClientImpl organizationClientMock;
  @Mock
  private OrganizationDTOMapper organizationDTOMapperMock;
  private OrganizationServiceImpl organizationService;
  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private EntityModelOrganization entityModelOrganization;
  private OrganizationDTO organizationDTO;
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");
    userOrganizationRoles.setRoles(Collections.singletonList("Admin"));

    userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));

    entityModelOrganization = new EntityModelOrganization();
    entityModelOrganization.setOrganizationId(123L);
    entityModelOrganization.setIpaCode("testIpaCode");
    entityModelOrganization.setOrgName("Test Organization");

    organizationDTO = OrganizationDTO.builder()
      .organizationId(123L)
      .ipaCode("testIpaCode")
      .orgName("Test Organization")
      .operatorRole(Collections.singletonList("Admin"))
      .build();

    organizationService = new OrganizationServiceImpl(organizationClientMock, organizationDTOMapperMock);
  }

  @Test
  void testGetOrganizations() {
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);
    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(any(EntityModelOrganization.class), anyList()))
      .thenReturn(organizationDTO);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(123L, result.getFirst().getOrganizationId());
    assertEquals("testIpaCode", result.getFirst().getIpaCode());
    assertEquals("Test Organization", result.getFirst().getOrgName());
    assertEquals(Collections.singletonList("Admin"), result.getFirst().getOperatorRole());
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

    assertTrue(result.isEmpty());
  }


  @Test
  void testGetOrganizations_NotFound() {
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(null);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertTrue(result.isEmpty());
  }

}
