package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsRegistryException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryMapper;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private AssessmentsRegistryMapper assessmentsRegistryMapperMock;
  @Mock
  private AssessmentsRegistryService assessmentsRegistryServiceMock;
  @Mock
  private AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapperMock;

  private AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    assessmentsRegistryRetrieverService = new AssessmentsRegistryRetrieverServiceImpl(debtPositionTypeOrgServiceMock,
      debtPositionTypeOrgRetrieverServiceMock, assessmentsRegistryServiceMock, assessmentsRegistryMapperMock, assessmentsRegistryDTOMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgServiceMock,
      debtPositionTypeOrgRetrieverServiceMock, assessmentsRegistryServiceMock, assessmentsRegistryMapperMock, assessmentsRegistryDTOMapperMock);
  }

  @Test
  void givenPopulatedDebtPositionTypeOrgCodesWhenGetAssessmentsRegistriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);
    filters.setDebtPositionTypeOrgCodes(null);
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    PageRequest pageable = PageRequest.of(0, 10);
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(PagedModelAssessmentsRegistry.class);
    PagedAssessmentsRegistry expectedResult = podamFactory.manufacturePojo(PagedAssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(filters.getOrganizationId(), debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(filters, pageable, accessToken))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, debtPositionTypeOrgCode, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      verifyNoInteractions(debtPositionTypeOrgServiceMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenNoFilterDebtPositionTypeOrgCodesWhenGetAssessmentsRegistriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);
    filters.setDebtPositionTypeOrgCodes(null);

    PageRequest pageable = PageRequest.of(0, 10);
    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);
    Set<String> expectedDebtPositionTypeOrgCodes = debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(PagedModelAssessmentsRegistry.class);
    PagedAssessmentsRegistry expectedResult = podamFactory.manufacturePojo(PagedAssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(debtPositionTypeOrgs);
      ArgumentCaptor<AssessmentsRegistryFiltersDTO> filtersCaptor = ArgumentCaptor.forClass(AssessmentsRegistryFiltersDTO.class);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
          filtersCaptor.capture(), Mockito.eq(pageable), Mockito.eq(accessToken)))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
      List<AssessmentsRegistryFiltersDTO> filterValues = filtersCaptor.getAllValues();
      assertEquals(1, filterValues.size());
      TestUtils.reflectionEqualsByName(filters, filterValues.get(0), "debtPositionTypeOrgCodes");
      assertEquals(expectedDebtPositionTypeOrgCodes, filterValues.get(0).getDebtPositionTypeOrgCodes());

      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgCodesWhenGetAssessmentsRegistriesThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);
    filters.setDebtPositionTypeOrgCodes(null);

    PageRequest pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(null);

      assertThrows(ResourceNotFoundException.class, () -> assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken));

      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock,assessmentsRegistryMapperMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetAssessmentsRegistriesThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Pageable pageable = PageRequest.of(0, 10);

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
      verifyNoInteractions(debtPositionTypeOrgServiceMock);
    }
  }

  @Test
  void givenValidAssessmentRegistryWhenCreateAssessmentsRegistryThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setAssessmentRegistryId(null);
    assessmentsRegistry.setOrganizationId(organizationId);
    AssessmentsRegistry expectedResult = podamFactory.manufacturePojo(AssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId,assessmentsRegistry.getDebtPositionTypeOrgCode(),loggedUser.getMappedExternalUserId(),accessToken);
      Mockito.when(assessmentsRegistryServiceMock.createAssessmentsRegistry(assessmentsRegistry, accessToken))
              .thenReturn(expectedResult);

      AssessmentsRegistry result = assessmentsRegistryRetrieverService.createAssessmentsRegistry(organizationId,assessmentsRegistry,loggedUser,accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenInvalidAssessmentRegistryWhenCreateAssessmentsRegistryThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      assertThrows(InvalidAssessmentsRegistryException.class, ()-> assessmentsRegistryRetrieverService.createAssessmentsRegistry(organizationId,assessmentsRegistry,loggedUser,accessToken));

      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenWrongOrganizationIdWhenCreateAssessmentsRegistryThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setAssessmentRegistryId(null);
    assessmentsRegistry.setOrganizationId(organizationId+1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      assertThrows(InvalidAssessmentsRegistryException.class, ()-> assessmentsRegistryRetrieverService.createAssessmentsRegistry(organizationId,assessmentsRegistry,loggedUser,accessToken));

      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidRequestWhenGetAssessmentsRegistryThenReturnAssessmentsRegistryDTO() {
    Long organizationId = 1L;
    Long assessmentRegistryId = 100L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryDTO expectedDTO = new AssessmentsRegistryDTO();
    expectedDTO.setDebtPositionTypeOrgCode("CODE123");

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken))
      .thenReturn(assessmentsRegistry);
    Mockito.when(assessmentsRegistryDTOMapperMock.map(assessmentsRegistry))
      .thenReturn(expectedDTO);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(inv -> null);

      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock)
        .validateOperator(organizationId, "CODE123", loggedUser.getMappedExternalUserId(), accessToken);

      AssessmentsRegistryDTO result = assessmentsRegistryRetrieverService.getAssessmentsRegistry(organizationId, assessmentRegistryId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedDTO, result);
      authMock.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenUnauthorizedUserWhenGetAssessmentsRegistryThenThrowAuthorizationDeniedException() {
    Long organizationId = 1L;
    Long assessmentRegistryId = 100L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        assessmentsRegistryRetrieverService.getAssessmentsRegistry(organizationId, assessmentRegistryId, loggedUser, accessToken));

      authMock.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(assessmentsRegistryServiceMock, assessmentsRegistryDTOMapperMock, debtPositionTypeOrgRetrieverServiceMock);
    }
  }

  @Test
  void givenInvalidDebtPositionTypeOrgCodeWhenGetAssessmentsRegistryThenThrowResourceNotFoundException() {
    Long organizationId = 1L;
    Long assessmentRegistryId = 100L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryDTO dto = new AssessmentsRegistryDTO();
    dto.setDebtPositionTypeOrgCode("INVALID_CODE");

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken))
      .thenReturn(assessmentsRegistry);
    Mockito.when(assessmentsRegistryDTOMapperMock.map(assessmentsRegistry))
      .thenReturn(dto);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      Mockito.doThrow(new ResourceNotFoundException("Operator not found"))
        .when(debtPositionTypeOrgRetrieverServiceMock)
        .validateOperator(organizationId, "INVALID_CODE", loggedUser.getMappedExternalUserId(), accessToken);

      assertThrows(ResourceNotFoundException.class, () ->
        assessmentsRegistryRetrieverService.getAssessmentsRegistry(organizationId, assessmentRegistryId, loggedUser, accessToken));

      Mockito.verifyNoInteractions(assessmentsRegistryMapperMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }
}

