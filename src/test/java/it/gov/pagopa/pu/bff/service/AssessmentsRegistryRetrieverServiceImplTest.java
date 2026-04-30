package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsRegistryException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryExtendedDTOMapper;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.*;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import jakarta.validation.Valid;
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
import org.springframework.web.server.ResponseStatusException;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryRetrieverServiceImplTest {
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private AssessmentsRegistryExtendedDTOMapper assessmentsRegistryExtendedDTOMapperMock;
  @Mock
  private AssessmentsRegistryService assessmentsRegistryServiceMock;
  @Mock
  private AssessmentsRegistryDTOMapper assessmentsRegistryDTOMapperMock;

  private AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    assessmentsRegistryRetrieverService = new AssessmentsRegistryRetrieverServiceImpl(
      debtPositionTypeOrgRetrieverServiceMock, assessmentsRegistryServiceMock, assessmentsRegistryExtendedDTOMapperMock, assessmentsRegistryDTOMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionTypeOrgRetrieverServiceMock, assessmentsRegistryServiceMock, assessmentsRegistryExtendedDTOMapperMock, assessmentsRegistryDTOMapperMock);
  }

  @Test
  void givenPopulatedDebtPositionTypeOrgCodesWhenGetAssessmentsRegistriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);
    filters.setDebtPositionTypeOrgCodes(null);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    Map<String,DebtPositionTypeOrg> debtPositionTypeOrgMap = Map.of(debtPositionTypeOrg.getCode(),debtPositionTypeOrg);

    PageRequest pageable = PageRequest.of(0, 10);
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(PagedModelAssessmentsRegistry.class);
    PagedAssessmentsRegistry expectedResult = podamFactory.manufacturePojo(PagedAssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgByCode(filters.getOrganizationId(), debtPositionTypeOrg.getCode(), loggedUser.getMappedExternalUserId(), accessToken))
              .thenReturn(debtPositionTypeOrg);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(filters, pageable, accessToken))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryExtendedDTOMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry,debtPositionTypeOrgMap))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, debtPositionTypeOrg.getCode(), pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

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
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(PagedModelAssessmentsRegistry.class);
    Map<String,DebtPositionTypeOrg> debtPositionTypeOrgs = new HashMap<>();
    for (@Valid AssessmentsRegistry assessmentsRegistry : pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries()) {
      DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
      debtPositionTypeOrg.setCode(assessmentsRegistry.getDebtPositionTypeOrgCode());
      debtPositionTypeOrgs.put(assessmentsRegistry.getDebtPositionTypeOrgCode(),debtPositionTypeOrg);
    }
    PagedAssessmentsRegistry expectedResult = podamFactory.manufacturePojo(PagedAssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), null, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn((new ArrayList<>(debtPositionTypeOrgs.values())));
      ArgumentCaptor<AssessmentsRegistryFiltersDTO> filtersCaptor = ArgumentCaptor.forClass(AssessmentsRegistryFiltersDTO.class);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
          filtersCaptor.capture(), Mockito.eq(pageable), Mockito.eq(accessToken)))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryExtendedDTOMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry, debtPositionTypeOrgs))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
      List<AssessmentsRegistryFiltersDTO> filterValues = filtersCaptor.getAllValues();
      assertEquals(1, filterValues.size());
      TestUtils.reflectionEqualsByName(filters, filterValues.get(0), "debtPositionTypeOrgCodes");
      assertEquals(debtPositionTypeOrgs.keySet(), filterValues.get(0).getDebtPositionTypeOrgCodes());

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

      Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), null, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(null);

      assertThrows(ResourceNotFoundException.class, () -> assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken));

      verifyNoInteractions(assessmentsRegistryServiceMock, assessmentsRegistryExtendedDTOMapperMock);
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
    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    assessmentsRegistry.setDebtPositionTypeOrgCode("CODE123");
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken))
      .thenReturn(assessmentsRegistry);
    Mockito.when(assessmentsRegistryDTOMapperMock.map(assessmentsRegistry,debtPositionTypeOrg.getDescription()))
      .thenReturn(expectedDTO);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(inv -> null);

      Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgByCode(
              organizationId, "CODE123", loggedUser.getMappedExternalUserId(), accessToken)
          ).thenReturn(debtPositionTypeOrg);

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

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    assessmentsRegistry.setDebtPositionTypeOrgCode("INVALID_CODE");
    Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken))
      .thenReturn(assessmentsRegistry);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      Mockito.doThrow(new ResourceNotFoundException("OPERATOR_NOT_FOUND", "Operator not found"))
        .when(debtPositionTypeOrgRetrieverServiceMock)
        .getDebtPositionTypeOrgByCode(organizationId, "INVALID_CODE", loggedUser.getMappedExternalUserId(), accessToken);

      assertThrows(ResourceNotFoundException.class, () ->
        assessmentsRegistryRetrieverService.getAssessmentsRegistry(organizationId, assessmentRegistryId, loggedUser, accessToken));

      Mockito.verifyNoInteractions(assessmentsRegistryDTOMapperMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidAssessmentRegistryWhenUpdateAssessmentsRegistryThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(organizationId);
    body.setStatus(AssessmentsRegistryStatus.ACTIVE);

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode(body.getDebtPositionTypeOrgCode());

    AssessmentsRegistry expected = podamFactory.manufacturePojo(AssessmentsRegistry.class);

    PagedModelAssessmentsRegistryEmbedded embedded = new PagedModelAssessmentsRegistryEmbedded();
    embedded.setAssessmentsRegistries(List.of(existing));

    PageMetadata pageMetadata = new PageMetadata(1L, 0L, 1L, 1L);
    PagedModelAssessmentsRegistry pagedResult = new PagedModelAssessmentsRegistry();
    pagedResult.setEmbedded(embedded);
    pagedResult.setPage(pageMetadata);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);
      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
        organizationId, body.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
        Mockito.any(), Mockito.any(), Mockito.eq(accessToken))).thenReturn(pagedResult);
      Mockito.when(assessmentsRegistryServiceMock.updateAssessmentsRegistry(body, accessToken)).thenReturn(expected);

      AssessmentsRegistry result = assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
        organizationId, assessmentRegistryId, body, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
      authMock.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenMultipleActiveRegistriesWhenUpdateAssessmentsRegistryThenThrowConflict() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(organizationId);
    body.setStatus(AssessmentsRegistryStatus.ACTIVE);

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode(body.getDebtPositionTypeOrgCode());

    AssessmentsRegistry reg1 = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    reg1.setAssessmentRegistryId(assessmentRegistryId);
    AssessmentsRegistry reg2 = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    reg2.setAssessmentRegistryId(999L);

    PagedModelAssessmentsRegistryEmbedded embedded = new PagedModelAssessmentsRegistryEmbedded();
    embedded.setAssessmentsRegistries(List.of(reg1, reg2));

    PageMetadata pageMetadata = new PageMetadata(2L, 0L, 2L, 1L);
    PagedModelAssessmentsRegistry pagedResult = new PagedModelAssessmentsRegistry();
    pagedResult.setEmbedded(embedded);
    pagedResult.setPage(pageMetadata);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);
      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
        organizationId, body.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
        Mockito.any(), Mockito.any(), Mockito.eq(accessToken))).thenReturn(pagedResult);

      assertThrows(ResponseStatusException.class, () ->
        assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
          organizationId, assessmentRegistryId, body, loggedUser, accessToken));
    }
  }

  @Test
  void givenAnotherActiveRegistryExistsWhenUpdateAssessmentsRegistryThenThrowConflict() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(organizationId);
    body.setStatus(AssessmentsRegistryStatus.ACTIVE);

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode(body.getDebtPositionTypeOrgCode());

    AssessmentsRegistry conflicting = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    conflicting.setAssessmentRegistryId(999L);

    PagedModelAssessmentsRegistryEmbedded embedded = new PagedModelAssessmentsRegistryEmbedded();
    embedded.setAssessmentsRegistries(List.of(conflicting));

    PagedModelAssessmentsRegistry pagedResult = new PagedModelAssessmentsRegistry();
    pagedResult.setEmbedded(embedded);
    pagedResult.setPage(new PageMetadata(1L, 0L, 1L, 1L));

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);
      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
        organizationId, body.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
        Mockito.any(), Mockito.any(), Mockito.eq(accessToken))).thenReturn(pagedResult);

      assertThrows(ResponseStatusException.class, () ->
        assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
          organizationId, assessmentRegistryId, body, loggedUser, accessToken));
    }
  }

  @Test
  void givenWrongOrganizationIdWhenUpdateAssessmentsRegistryThenThrowResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(2L);

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode(body.getDebtPositionTypeOrgCode());

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);

      assertThrows(InvalidAssessmentsRegistryException.class, () ->
        assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
          organizationId, assessmentRegistryId, body, loggedUser, accessToken));
    }
  }

  @Test
  void givenModifiedDebtPositionTypeOrgCodeWhenUpdateAssessmentsRegistryThenThrowIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(organizationId);
    body.setDebtPositionTypeOrgCode("NEW_CODE");

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode("OLD_CODE");

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);

      InvalidAssessmentsRegistryException ex =  assertThrows(InvalidAssessmentsRegistryException.class, () ->
        assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
          organizationId, assessmentRegistryId, body, loggedUser, accessToken));

      assertEquals("IMMUTABLE_DEBT_POSITION_TYPE_ORG_CODE", ex.getCode());
      assertTrue(ex.getMessage().contains("debtPositionTypeOrgCode cannot be modified"));
    }
  }

  @Test
  void givenMismatchedIdsWhenUpdateAssessmentsRegistryThenThrowIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(999L);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      InvalidAssessmentsRegistryException ex = assertThrows(
        InvalidAssessmentsRegistryException.class,
        () -> assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
          organizationId, assessmentRegistryId, body, loggedUser, accessToken));

      assertEquals("INVALID_ASSESSMENT_REGISTRY", ex.getCode());
      assertTrue(ex.getMessage().contains("assessmentRegistryId in path and body must match"));

      authMock.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenStatusInactiveWhenUpdateAssessmentsRegistryThenSkipCheckActiveRegistryUniqueness() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long assessmentRegistryId = 10L;

    AssessmentsRegistry body = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    body.setAssessmentRegistryId(assessmentRegistryId);
    body.setOrganizationId(organizationId);
    body.setStatus(AssessmentsRegistryStatus.INACTIVE);

    AssessmentsRegistry existing = new AssessmentsRegistry();
    existing.setAssessmentRegistryId(assessmentRegistryId);
    existing.setDebtPositionTypeOrgCode(body.getDebtPositionTypeOrgCode());

    AssessmentsRegistry expected = podamFactory.manufacturePojo(AssessmentsRegistry.class);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsRegistryServiceMock.getAssessmentsRegistry(assessmentRegistryId, accessToken)).thenReturn(existing);
      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
        organizationId, body.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.when(assessmentsRegistryServiceMock.updateAssessmentsRegistry(body, accessToken)).thenReturn(expected);

      AssessmentsRegistry result = assessmentsRegistryRetrieverService.updateAssessmentsRegistry(
        organizationId, assessmentRegistryId, body, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);

      Mockito.verify(assessmentsRegistryServiceMock, Mockito.never())
        .findAssessmentsRegistriesByFilters(Mockito.any(), Mockito.any(), Mockito.eq(accessToken));
    }
  }
}

