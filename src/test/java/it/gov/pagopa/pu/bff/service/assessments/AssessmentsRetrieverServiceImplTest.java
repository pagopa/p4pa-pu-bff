package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedAssessmentExtendedDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class AssessmentsRetrieverServiceImplTest {

  @Mock
  private AssessmentsClient assessmentsClientMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private PagedAssessmentExtendedDTOMapper pagedAssessmentExtendedDTOMapperMock;

  private AssessmentsRetrieverService assessmentsRetrieverService;
  private PodamFactory podamFactory;

  @BeforeEach
  void setUp() {
    assessmentsRetrieverService = new AssessmentsRetrieverServiceImpl(assessmentsClientMock, debtPositionTypeOrgRetrieverServiceMock, debtPositionTypeOrgServiceMock, pagedAssessmentExtendedDTOMapperMock);
    podamFactory = new PodamFactoryImpl();
  }

  @Test
  void givenDebtPositionTypeOrgCodePresentWhenGetPagedAssessmentsExtendedDTOThenReturnsMappedDTO() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
      .organizationId(1L)
      .build();

    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    PagedAssessmentsView pagedAssessmentsView = PagedAssessmentsView.builder()
            .content(List.of(assessments))
            .size(1L)
            .totalPages(1L)
            .totalElements(1L)
            .number(0)
            .build();

    AssessmentsExtendedDTO assessmentsExtendedDTO = AssessmentsExtendedDTO.builder()
            .assessmentId(assessments.getAssessmentId())
            .assessmentName(assessments.getAssessmentName())
            .creationDate(assessments.getCreationDate())
            .updateDate(assessments.getUpdateDate())
            .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
            .flagManualGeneration(assessments.getFlagManualGeneration())
            .organizationId(assessments.getOrganizationId())
            .links(assessments.getLinks())
            .printed(assessments.getPrinted())
            .status(assessments.getStatus())
            .updateOperatorExternalId(assessments.getUpdateOperatorExternalId())
            .updateTraceId(assessments.getUpdateTraceId())
            .descriptionDebtPositionTypeOrgCode("description")
            .build();

    PagedAssessmentsExtendedDTO pagedAssessmentsExtendedDTO = PagedAssessmentsExtendedDTO.builder()
            .content(List.of(assessmentsExtendedDTO))
            .size(1L)
            .totalPages(1L)
            .totalElements(1L)
            .number(0)
            .build();

    String debtPositionTypeOrg = "debtPositionTypeOrg";
    String accessToken = "accessToken";

    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);

    Map<String, String> debtPositionTypeOrgMap = Map.of(
            "debtPositionTypeOrg", ""
    );

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(filters.getOrganizationId(), debtPositionTypeOrg, loggedUser.getMappedExternalUserId(), accessToken);

      Mockito.when(assessmentsClientMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(debtPositionTypeOrgs);
      Mockito.when(pagedAssessmentExtendedDTOMapperMock.map(pagedAssessmentsView, debtPositionTypeOrgMap)).thenReturn(pagedAssessmentsExtendedDTO);


      PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(filters, debtPositionTypeOrg, Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(pagedAssessmentsExtendedDTO, result);
      Assertions.assertEquals(filters.getDebtPositionTypeOrgCodes(), Set.of(debtPositionTypeOrg));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenDebtPositionTypeOrgCodeAbsentWhenGetPagedAssessmentsExtendedDTOThenReturnsMappedDTO() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";
    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
            .organizationId(1L)
            .build();

    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    PagedAssessmentsView pagedAssessmentsView = PagedAssessmentsView.builder()
            .content(List.of(assessments))
            .size(1L)
            .totalPages(1L)
            .totalElements(1L)
            .number(0)
            .build();

    PagedAssessmentsExtendedDTO pagedAssessmentsExtendedDTO = PagedAssessmentsExtendedDTO.builder()
            .content(List.of())
            .size(1L)
            .totalPages(1L)
            .totalElements(1L)
            .number(0)
            .build();

    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrg.class);

    Map<String, String> debtPositionTypeOrgMap = debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream()
            .collect(Collectors.toMap(DebtPositionTypeOrg::getCode, DebtPositionTypeOrg::getDescription));


    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(debtPositionTypeOrgs);
      Mockito.when(assessmentsClientMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);
      Mockito.when(pagedAssessmentExtendedDTOMapperMock.map(pagedAssessmentsView, debtPositionTypeOrgMap)).thenReturn(pagedAssessmentsExtendedDTO);

      PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(filters, null, Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(pagedAssessmentsExtendedDTO, result);
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgFoundWhenGetPagedAssessmentsExtendedDTOThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
            .organizationId(1L)
            .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class, () ->
              assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(filters, null, Pageable.ofSize(1), loggedUser, accessToken));
    }
  }

  @Test
  void givenInvalidUserWhenGetPagedAssessmentsExtendedDTOThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
            .organizationId(1L)
            .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser))
              .thenThrow(new SecurityException("Unauthorized"));

      Assertions.assertThrows(SecurityException.class, () ->
              assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(filters, null, Pageable.ofSize(1), loggedUser, accessToken));
    }
  }

}
