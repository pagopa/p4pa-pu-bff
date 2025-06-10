package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryMapper;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
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

  private AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    assessmentsRegistryRetrieverService = new AssessmentsRegistryRetrieverServiceImpl(debtPositionTypeOrgServiceMock,
            debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock,assessmentsRegistryMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgServiceMock,
            debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock,assessmentsRegistryMapperMock);
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

      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(filters.getOrganizationId(),debtPositionTypeOrgCode,loggedUser.getMappedExternalUserId(),accessToken);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(filters,pageable, accessToken))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, debtPositionTypeOrgCode, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
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

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(),loggedUser.getMappedExternalUserId(),accessToken))
              .thenReturn(debtPositionTypeOrgs);
      ArgumentCaptor<AssessmentsRegistryFiltersDTO> filtersCaptor = ArgumentCaptor.forClass(AssessmentsRegistryFiltersDTO.class);
      Mockito.when(assessmentsRegistryServiceMock.findAssessmentsRegistriesByFilters(
              filtersCaptor.capture(),Mockito.eq(pageable), Mockito.eq(accessToken)))
        .thenReturn(pagedModelAssessmentsRegistry);
      Mockito.when(assessmentsRegistryMapperMock.mapToPagedAssessmentsRegistry(pagedModelAssessmentsRegistry))
        .thenReturn(expectedResult);

      PagedAssessmentsRegistry result = assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
      List<AssessmentsRegistryFiltersDTO> filterValues = filtersCaptor.getAllValues();
      assertEquals(1,filterValues.size());
      TestUtils.reflectionEqualsByName(filters,filterValues.get(0),"debtPositionTypeOrgCodes");
      assertEquals(expectedDebtPositionTypeOrgCodes,filterValues.get(0).getDebtPositionTypeOrgCodes());

      Mockito.verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock);
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

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(),loggedUser.getMappedExternalUserId(),accessToken))
              .thenReturn(null);

      assertThrows(ResourceNotFoundException.class, ()-> assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken));

      Mockito.verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock,assessmentsRegistryServiceMock,assessmentsRegistryMapperMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetAssessmentsRegistriesThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Pageable pageable = PageRequest.of(0,10);

    AssessmentsRegistryFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRegistryFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        assessmentsRegistryRetrieverService.getAssessmentsRegistries(filters, null, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
      Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
    }
  }
}

