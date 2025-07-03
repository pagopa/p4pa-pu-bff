package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationWithDebtPositionTypeOrgCountMapper;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationIdEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganizationEmbedded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrganizationRetrieverServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private OrganizationDTOMapper organizationDTOMapperMock;
  @Mock
  private OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapperMock;
  private OrganizationRetrieverServiceImpl organizationService;
  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private Organization entityModelOrganization;
  private OrganizationDTO organizationDTO;
  private final String accessToken = "TOKEN";
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");
    userOrganizationRoles.setRoles(Collections.singletonList("ROLE_ADMIN"));

    userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));
    userInfo.setBrokerId(1L);

    entityModelOrganization = new Organization();
    entityModelOrganization.setOrganizationId(123L);
    entityModelOrganization.setIpaCode("testIpaCode");
    entityModelOrganization.setOrgName("Test Organization");

    organizationDTO = OrganizationDTO.builder()
      .organizationId(123L)
      .ipaCode("testIpaCode")
      .orgName("Test Organization")
      .operatorRole(OrganizationDTO.OperatorRoleEnum.ROLE_ADMIN)
      .build();

    organizationService = new OrganizationRetrieverServiceImpl(
      authorizationServiceMock, organizationServiceMock,
      debtPositionTypeOrgServiceMock, organizationDTOMapperMock, organizationWithDebtPositionTypeOrgCountMapperMock);
  }

  @Test
  void testGetOrganizations() {
    Mockito.when(organizationServiceMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);
    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(any(Organization.class), anyList()))
      .thenReturn(organizationDTO);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(123L, result.getFirst().getOrganizationId());
    assertEquals("testIpaCode", result.getFirst().getIpaCode());
    assertEquals("Test Organization", result.getFirst().getOrgName());
    assertEquals(OrganizationDTO.OperatorRoleEnum.ROLE_ADMIN, result.getFirst().getOperatorRole());
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

    Mockito.when(organizationServiceMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(null);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertTrue(result.isEmpty());
  }

  @Test
  void testGetOrganizations_NotFound() {
    Mockito.when(organizationServiceMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(null);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenOrganizationsWithDebtPositionTyprOrgCountWhenGetOrganizationsWithDebtPositionTypeOrgCountThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    String orgName = "orgName";

    Mockito.doNothing().when(authorizationServiceMock)
      .validateBrokerAdminRole(any(UserInfo.class));

    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    List<Organization> organizations = List.of(new Organization());
    pagedModelOrganization.setEmbedded(
      PagedModelOrganizationEmbedded.builder().organizations(organizations)
        .build());
    Mockito.when(
      organizationServiceMock.getOrganizationByBrokerIdAndOrgName(same(userInfo.getBrokerId()),
        same(orgName), any(
          Pageable.class), eq(null))).thenReturn(pagedModelOrganization);

    CollectionModelDebtPositionTypeOrgCountByOrganizationId collectionDptoCountByOrgId = new CollectionModelDebtPositionTypeOrgCountByOrganizationId();
    List<DebtPositionTypeOrgCountByOrganizationId> debtPositionTypeOrgCountByOrganizationIds = List.of(
      new DebtPositionTypeOrgCountByOrganizationId());
    collectionDptoCountByOrgId.setEmbedded(
      CollectionModelDebtPositionTypeOrgCountByOrganizationIdEmbedded.builder()
        .debtPositionTypeOrgCountByOrganizationIds(
          debtPositionTypeOrgCountByOrganizationIds).build());
    Mockito.when(
      debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgCountByOrganizationId(
        anyList(), eq(null))).thenReturn(collectionDptoCountByOrgId);

    PagedOrganizationWithDebtPositionTypeOrgCount expectedResult = new PagedOrganizationWithDebtPositionTypeOrgCount();
    Mockito.when(
      organizationWithDebtPositionTypeOrgCountMapperMock.mapToPagedOrganizationWithDebtPositionTypeOrgCount(
        eq(organizations), eq(debtPositionTypeOrgCountByOrganizationIds), any())).thenReturn(expectedResult);

    PagedOrganizationWithDebtPositionTypeOrgCount result = organizationService.getOrganizationsWithDebtPositionTypeOrgCount(
      1L, orgName, Pageable.unpaged(), loggedUser, null);

    assertEquals(expectedResult, result);
  }

  @Test
  void givenNullPagedModelOrganizationWhenGetOrganizationsWithDebtPositionTypeOrgCountThenReturnEmptyContent() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    String orgName = "orgName";

    Mockito.doNothing().when(authorizationServiceMock)
      .validateBrokerAdminRole(any(UserInfo.class));

    Mockito.when(
      organizationServiceMock.getOrganizationByBrokerIdAndOrgName(same(userInfo.getBrokerId()),
        same(orgName), any(Pageable.class), eq(null))).thenReturn(null);

    PagedOrganizationWithDebtPositionTypeOrgCount result = organizationService.getOrganizationsWithDebtPositionTypeOrgCount(
      1L, orgName, Pageable.unpaged(), loggedUser, null);

    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
  }

  @Test
  void givenEmptyOrganizationsWhenGetOrganizationsWithDebtPositionTypeOrgCountThenReturnEmptyContent() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    String orgName = "orgName";

    Mockito.doNothing().when(authorizationServiceMock)
      .validateBrokerAdminRole(any(UserInfo.class));

    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    pagedModelOrganization.setEmbedded(
      PagedModelOrganizationEmbedded.builder().organizations(Collections.emptyList())
        .build());
    Mockito.when(
      organizationServiceMock.getOrganizationByBrokerIdAndOrgName(same(loggedUser.getBrokerId()),
        same(orgName), any(
          Pageable.class), eq(null))).thenReturn(pagedModelOrganization);

    PagedOrganizationWithDebtPositionTypeOrgCount result = organizationService.getOrganizationsWithDebtPositionTypeOrgCount(
      1L, orgName, Pageable.unpaged(), loggedUser, null);

    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
  }

  @Test
  void givenMatchingBrokerIdWhenGetOrgFiscalCodeThenOk(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(loggedUser.getBrokerId());

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organization.getOrganizationId(),accessToken))
            .thenReturn(organization);

    String result = organizationService.getOrgFiscalCode(organization.getOrganizationId(), loggedUser, accessToken);

    assertNotNull(result);
    assertEquals(organization.getOrgFiscalCode(),result);
  }

  @Test
  void givenNoMatchingBrokerIdWhenGetOrgFiscalCodeThenResourceNotFound(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(loggedUser.getBrokerId()+1);
    Long organizationId = organization.getOrganizationId();

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken))
            .thenReturn(organization);

    assertThrows(ResourceNotFoundException.class,()->organizationService.getOrgFiscalCode(organizationId, loggedUser, accessToken));
  }

  @Test
  void givenNoOrganizationBrokerIdWhenGetOrgFiscalCodeThenResourceNotFound(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(null);
    Long organizationId = organization.getOrganizationId();

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken))
            .thenReturn(organization);

    assertThrows(ResourceNotFoundException.class,()->organizationService.getOrgFiscalCode(organizationId, loggedUser, accessToken));
  }

  @Test
  void givenNoOrganizationWhenGetOrgFiscalCodeThenResourceNotFound(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(1L);
    Long organizationId = 2L;

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken))
            .thenReturn(null);

    assertThrows(ResourceNotFoundException.class,()->organizationService.getOrgFiscalCode(organizationId, loggedUser, accessToken));
  }
}
