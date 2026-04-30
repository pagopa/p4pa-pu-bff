package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationDetailMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationWithDebtPositionTypeOrgCountMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationIdEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganizationEmbedded;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
  @Mock
  private PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock;
  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private OrganizationDetailMapper organizationDetailMapperMock;

  private OrganizationRetrieverServiceImpl organizationService;
  private UserInfo userInfo;
  private UserOrganizationRoles userOrganizationRoles;
  private Organization entityModelOrganization;
  private OrganizationDTO organizationDTO;
  private final String accessToken = "TOKEN";
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @AfterEach
  void mockito(){
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock,
      organizationServiceMock,
      debtPositionTypeOrgServiceMock,
      organizationDTOMapperMock,
      organizationWithDebtPositionTypeOrgCountMapperMock,
      authzServiceMock,
      pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock,
      organizationDetailMapperMock);
  }

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
      .operatorRole(OperatorRole.ROLE_ADMIN)
      .build();

    organizationService = new OrganizationRetrieverServiceImpl(
      authorizationServiceMock,
      organizationServiceMock,
      debtPositionTypeOrgServiceMock,
      organizationDTOMapperMock,
      organizationWithDebtPositionTypeOrgCountMapperMock,
      authzServiceMock,
      pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock,
      organizationDetailMapperMock);
  }

  @Test
  void testGetOrganizations() {
    userInfo.setBrokerId(1L);
    entityModelOrganization.setBrokerId(1L);

    Mockito.when(organizationServiceMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);
    Mockito.when(organizationDTOMapperMock.mapToOrganizationDTO(any(Organization.class), anyList()))
      .thenReturn(organizationDTO);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertEquals(1, result.size());
    assertEquals(123L, result.getFirst().getOrganizationId());
    assertEquals("testIpaCode", result.getFirst().getIpaCode());
    assertEquals("Test Organization", result.getFirst().getOrgName());
    assertEquals(OperatorRole.ROLE_ADMIN, result.getFirst().getOperatorRole());
  }

  @Test
  void testGetOrganizationsWhenBrokerMismatch() {
    userInfo.setBrokerId(10L);
    entityModelOrganization.setBrokerId(20L);

    Mockito.when(organizationServiceMock.getOrganizationByIpaCode(anyString(), anyString()))
      .thenReturn(entityModelOrganization);

    List<OrganizationDTO> result = organizationService.getOrganizations(userInfo, accessToken);

    assertTrue(result.isEmpty());
    Mockito.verify(organizationDTOMapperMock, Mockito.never()).mapToOrganizationDTO(any(), anyList());
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

  @Test
  void givenPagedModelOrganizationWhenGetOrganizationsByBrokerIdAndFiltersThenReturnCorrectPagedDto() {
    // Given
    Long expectedOrgId = 123L;
    UserOrganizationRoles role = new UserOrganizationRoles();
    role.setOrganizationId(expectedOrgId);
    userInfo.setOrganizations(List.of(role));
    String orgName = "TestOrg";
    String ipaCode = "IPA123";
    String orgFiscalCode = "FISC123";
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    List<Organization> organizationsList = Collections.singletonList(organization);

    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    pagedModelOrganization.setEmbedded(
      PagedModelOrganizationEmbedded.builder().organizations(organizationsList).build());

    Set<Long> allowedOrganizationIds = Set.of(expectedOrgId);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(organizationServiceMock.getOrganizationsByBrokerIdAndFilters(
        userInfo.getBrokerId(), orgName, ipaCode, orgFiscalCode, allowedOrganizationIds, Pageable.ofSize(1), accessToken))
      .thenReturn(pagedModelOrganization);

    DebtPositionTypeOrgCountByOrganizationId mockDptoCount = new DebtPositionTypeOrgCountByOrganizationId();
    mockDptoCount.setOrganizationId(123L);
    mockDptoCount.setActiveOrganizations(10);
    CollectionModelDebtPositionTypeOrgCountByOrganizationId collectionModelDebtPositionTypeOrgCountByOrganizationId = new CollectionModelDebtPositionTypeOrgCountByOrganizationId();
    collectionModelDebtPositionTypeOrgCountByOrganizationId.setEmbedded(new CollectionModelDebtPositionTypeOrgCountByOrganizationIdEmbedded());
    collectionModelDebtPositionTypeOrgCountByOrganizationId.getEmbedded().setDebtPositionTypeOrgCountByOrganizationIds(Collections.singletonList(mockDptoCount));
    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgCountByOrganizationId(
        List.of(123L), accessToken))
      .thenReturn(collectionModelDebtPositionTypeOrgCountByOrganizationId);

    OperatorsPage mockOperatorsPage = new OperatorsPage();
    mockOperatorsPage.setContent(Collections.singletonList(new OperatorDTO()));
    mockOperatorsPage.setTotalElements(5);
    Mockito.when(authzServiceMock.getOrganizationOperators(
        "testIpaCode", null, null, null, 0, 1, accessToken))
      .thenReturn(mockOperatorsPage);

    Map<Long, Integer> expectedDptoMap = Map.of(123L, 10);
    Map<Long, OperatorsPage> expectedOperatorsMap = Map.of(123L, mockOperatorsPage);
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount expectedResult = new PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount();
    expectedResult.setTotalElements(5L);

    Mockito.when(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock.map(
        pagedModelOrganization, expectedDptoMap, expectedOperatorsMap))
      .thenReturn(expectedResult);

    // When
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result = organizationService.getOrganizationsByBrokerIdAndFilters(userInfo, orgName, ipaCode, orgFiscalCode, Pageable.ofSize(1), accessToken);

    // Then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void givenNullOrganizationsWhenGetOrganizationsByBrokerIdAndFiltersThenReturnEmptyPagedDto() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);

    Mockito.when(organizationServiceMock.getOrganizationsByBrokerIdAndFilters(
        eq(userInfo.getBrokerId()), isNull(), isNull(), isNull(), anySet(), eq(pageable), eq(accessToken)))
      .thenReturn(null);

    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount expectedResult = PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount.builder()
      .content(Collections.emptyList())
      .size(0L)
      .totalPages(0L)
      .totalElements(0L)
      .number(0L)
      .build();

    Mockito.when(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock.map(
        Mockito.isNull(), eq(Collections.emptyMap()), eq(Collections.emptyMap())))
      .thenReturn(expectedResult);

    // When
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result = organizationService.getOrganizationsByBrokerIdAndFilters(userInfo, null, null, null, pageable, accessToken);

    // Then
    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
    assertEquals(0L, result.getSize());
    assertEquals(0L, result.getTotalElements());
    assertEquals(0L, result.getTotalPages());
    assertEquals(0L, result.getNumber());
  }

  @Test
  void givenEmbeddedNullWhenGetOrganizationsByBrokerIdAndFiltersThenReturnEmptyPagedDto() {
    // Given
    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    pagedModelOrganization.setEmbedded(null);
    Pageable pageable = Pageable.ofSize(1);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(organizationServiceMock.getOrganizationsByBrokerIdAndFilters(
        eq(userInfo.getBrokerId()), isNull(), isNull(), isNull(), anySet(), eq(pageable), eq(accessToken)))
      .thenReturn(pagedModelOrganization);

    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount expectedResult =
      new PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount();
    Mockito.when(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock
        .map(pagedModelOrganization, Collections.emptyMap(), Collections.emptyMap()))
      .thenReturn(expectedResult);

    // When
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result =
      organizationService.getOrganizationsByBrokerIdAndFilters(
        userInfo, null, null, null, pageable, accessToken);

    // Then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void givenOrganizationsNullWhenGetOrganizationsByBrokerIdAndFiltersThenReturnEmptyPagedDto() {
    // Given
    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    pagedModelOrganization.setEmbedded(new PagedModelOrganizationEmbedded());
    pagedModelOrganization.getEmbedded().setOrganizations(null);
    Pageable pageable = Pageable.ofSize(1);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(organizationServiceMock.getOrganizationsByBrokerIdAndFilters(
        eq(userInfo.getBrokerId()), isNull(), isNull(), isNull(), anySet(), eq(pageable), eq(accessToken)))
      .thenReturn(pagedModelOrganization);

    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount expectedResult =
      new PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount();
    Mockito.when(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock
        .map(pagedModelOrganization, Collections.emptyMap(), Collections.emptyMap()))
      .thenReturn(expectedResult);

    // When
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result =
      organizationService.getOrganizationsByBrokerIdAndFilters(
        userInfo, null, null, null, pageable, accessToken);

    // Then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void givenEmptyOrganizationsListWhenGetOrganizationsByBrokerIdAndFiltersThenReturnEmptyPagedDto() {
    // Given
    PagedModelOrganization pagedModelOrganization = new PagedModelOrganization();
    pagedModelOrganization.setEmbedded(
      PagedModelOrganizationEmbedded.builder().organizations(Collections.emptyList()).build());
    Pageable pageable = Pageable.ofSize(1);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(userInfo);
    Mockito.when(organizationServiceMock.getOrganizationsByBrokerIdAndFilters(
        eq(userInfo.getBrokerId()), isNull(), isNull(), isNull(), anySet(), eq(pageable), eq(accessToken)))
      .thenReturn(pagedModelOrganization);

    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount expectedResult =
      new PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount();
    Mockito.when(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperMock
        .map(pagedModelOrganization, Collections.emptyMap(), Collections.emptyMap()))
      .thenReturn(expectedResult);

    // When
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result =
      organizationService.getOrganizationsByBrokerIdAndFilters(
        userInfo, null, null, null, pageable, accessToken);

    // Then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void givenAdminRoleWhenUpdateOrganizationThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    OrganizationDetailDTO orgDTO = podamFactory.manufacturePojo(OrganizationDetailDTO.class);
    orgDTO.setOrganizationId(organizationId);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(orgDTO.getBrokerId());
    organization.setExternalOrganizationId(orgDTO.getExternalOrganizationId());
    organization.setIpaCode(orgDTO.getIpaCode());
    organization.setOrgFiscalCode(orgDTO.getOrgFiscalCode());
    organization.setOrgName(orgDTO.getOrgName());
    organization.setOrgTypeCode(orgDTO.getOrgTypeCode());

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);
    doNothing().when(organizationServiceMock).updateOrganization(orgDTO, accessToken);

    organizationService.updateOrganization(organizationId, orgDTO, loggedUser, accessToken);
  }

  @Test
  void givenUpdatedReadOnlyFieldWhenUpdateOrganizationThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    OrganizationDetailDTO orgDTO = podamFactory.manufacturePojo(OrganizationDetailDTO.class);
    orgDTO.setOrganizationId(organizationId);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setBrokerId(orgDTO.getBrokerId());
    organization.setExternalOrganizationId(orgDTO.getExternalOrganizationId());
    organization.setIpaCode(orgDTO.getIpaCode());
    organization.setOrgFiscalCode(orgDTO.getOrgFiscalCode());
    organization.setOrgName(orgDTO.getOrgName());
    organization.setOrgTypeCode(orgDTO.getOrgTypeCode()+"old");

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    Assertions.assertThrows(ValidationException.class,() -> organizationService.updateOrganization(organizationId, orgDTO, loggedUser, accessToken));
  }

  @Test
  void givenNoExistingOrganizationWhenUpdateOrganizationThenResourceNotFound() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    OrganizationDetailDTO orgDTO = podamFactory.manufacturePojo(OrganizationDetailDTO.class);
    orgDTO.setOrganizationId(organizationId);

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class,() -> organizationService.updateOrganization(organizationId, orgDTO, loggedUser, accessToken));
  }

  @Test
  void givenWrongOrganizationIdWhenUpdateOrganizationThenInvalidOrganizationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    OrganizationDetailDTO orgDTO = podamFactory.manufacturePojo(OrganizationDetailDTO.class);
    orgDTO.setOrganizationId(organizationId+1);

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);

    Assertions.assertThrows(InvalidOrganizationException.class,() -> organizationService.updateOrganization(organizationId, orgDTO, loggedUser, accessToken));
  }

  @Test
  void givenExistingOrganizationIdWhenGetOrganizationDetailThenReturnMappedOrganizationDetailDTO() {
    Long organizationId = 123L;
    String ipaCode = "IPA123";

    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    organization.setIpaCode(ipaCode);

    OrganizationDetailDTO orgDetail = podamFactory.manufacturePojo(OrganizationDetailDTO.class);
    OrganizationDetail expectedDetail = podamFactory.manufacturePojo(OrganizationDetail.class);

    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
      .thenReturn(organization);

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(organizationId,userInfo);

    when(organizationServiceMock.getOrganizationDetail(organizationId, accessToken))
      .thenReturn(orgDetail);

    when(organizationDetailMapperMock.mapToBffDTO(orgDetail))
      .thenReturn(expectedDetail);

    DebtPositionTypeOrgCountByOrganizationId mockDptoCount = new DebtPositionTypeOrgCountByOrganizationId();
    mockDptoCount.setOrganizationId(organizationId);
    mockDptoCount.setActiveOrganizations(7);

    CollectionModelDebtPositionTypeOrgCountByOrganizationId collectionModel = new CollectionModelDebtPositionTypeOrgCountByOrganizationId();
    collectionModel.setEmbedded(new CollectionModelDebtPositionTypeOrgCountByOrganizationIdEmbedded());
    collectionModel.getEmbedded().setDebtPositionTypeOrgCountByOrganizationIds(Collections.singletonList(mockDptoCount));

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgCountByOrganizationId(
      List.of(organizationId), accessToken))
      .thenReturn(collectionModel);

    OperatorsPage mockOperatorsPage = new OperatorsPage();
    mockOperatorsPage.setContent(Collections.singletonList(new OperatorDTO()));
    mockOperatorsPage.setTotalElements(5);

    when(authzServiceMock.getOrganizationOperators(ipaCode, null, null, null, 0, 1, accessToken))
      .thenReturn(mockOperatorsPage);

    OrganizationDetail result = organizationService.getOrganizationDetail(organizationId, userInfo, accessToken);

    assertNotNull(result);
    assertEquals(expectedDetail, result);
    assertEquals(7, result.getDebtPositionTypeOrgCount());
    assertEquals(5, result.getOperatorsCount());
  }

  @Test
  void givenNonExistingOrganizationIdWhenGetOrganizationDetailThenThrowResourceNotFoundException() {
    Long organizationId = 123L;

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(organizationId, userInfo);

    when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken))
      .thenReturn(null);

    ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
      () -> organizationService.getOrganizationDetail(organizationId, userInfo, accessToken));

    assertTrue(ex.getMessage().contains("Organization having organizationId " + organizationId + " not found"));
  }
}
