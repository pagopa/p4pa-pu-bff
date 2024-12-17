package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.OrganizationClientImpl;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
      .operatorRole("Admin")
      .build();

    organizationService = new OrganizationServiceImpl(organizationClientMock, organizationDTOMapperMock);
  }

  @Test
  void testGetOrganizations() {
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);
    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(any(EntityModelOrganization.class), any(List.class)))
      .thenReturn(organizationDTO);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(123L, result.get(0).getOrganizationId());
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
    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(null, Collections.emptyList()))
      .thenReturn(new OrganizationDTO());

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertNull(result.get(0).getOrganizationId());
    assertNull(result.get(0).getIpaCode());
    assertNull(result.get(0).getOrgName());
    assertNull(result.get(0).getOperatorRole());
  }

  @Test
  void testGetOrganizations_NotFound() {
    Mockito.when(organizationClientMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(null);

    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(null, Collections.singletonList("Admin")))
      .thenReturn(OrganizationDTO.builder()
        .organizationId(null)
        .ipaCode(null)
        .orgName(null)
        .operatorRole("Admin")
        .build());

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertNull(result.get(0).getOrganizationId());
    assertNull(result.get(0).getIpaCode());
    assertNull(result.get(0).getOrgName());
    assertEquals("Admin", result.get(0).getOperatorRole());
  }

}
