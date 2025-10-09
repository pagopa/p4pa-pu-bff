package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.exception.InvalidAssessmentsDetailException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentExtendedDTOMapper;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRowsDetailMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.*;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class AssessmentsRetrieverServiceImplTest {

  @Mock
  private AssessmentsService assessmentsServiceMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private AssessmentExtendedDTOMapper assessmentExtendedDTOMapperMock;
  @Mock
  private AssessmentsRowsDetailMapper assessmentsRowsDetailMapperMock;
  @Mock
  private AuthzService authzServiceMock;
  private AssessmentsRetrieverService assessmentsRetrieverService;
  private PodamFactory podamFactory;

  @BeforeEach
  void setUp() {
    assessmentsRetrieverService = new AssessmentsRetrieverServiceImpl(assessmentsServiceMock, debtPositionTypeOrgRetrieverServiceMock, debtPositionTypeOrgServiceMock, assessmentExtendedDTOMapperMock, assessmentsRowsDetailMapperMock, authzServiceMock);
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
     assessments.setDebtPositionTypeOrgCode("debtPositionTypeOrg");

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

     String debtPositionTypeOrg = "debtPositionTypeOrg";
     String accessToken = "accessToken";

     DebtPositionTypeOrg debtPositionTypeOrgObj = new DebtPositionTypeOrg();
     debtPositionTypeOrgObj.setCode(debtPositionTypeOrg);
     debtPositionTypeOrgObj.setDescription("description");

     Map<String, String> debtPositionTypeOrgMap = Map.of(debtPositionTypeOrg, "description");

     try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
     authorizationServiceMockedStatic.when(() ->
                   AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)
                   ).thenAnswer(a -> null);

     Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
                   filters.getOrganizationId(), debtPositionTypeOrg, loggedUser.getMappedExternalUserId(), accessToken
                   );

     Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(
                   filters.getOrganizationId(), debtPositionTypeOrg, loggedUser.getMappedExternalUserId(), accessToken
                   )).thenReturn(debtPositionTypeOrgObj);

     Mockito.when(assessmentsServiceMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken))
     .thenReturn(pagedAssessmentsView);

     Mockito.when(assessmentExtendedDTOMapperMock.mapToPagedAssessmentsExtendedDTO(pagedAssessmentsView, debtPositionTypeOrgMap))
     .thenReturn(pagedAssessmentsExtendedDTO);

     PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
                   filters, debtPositionTypeOrg, Pageable.ofSize(1), loggedUser, accessToken
                   );

     Assertions.assertNotNull(result);
     Assertions.assertEquals(pagedAssessmentsExtendedDTO, result);
     Assertions.assertEquals(Set.of(debtPositionTypeOrg), filters.getDebtPositionTypeOrgCodes());

     authorizationServiceMockedStatic.verify(() ->
                   AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)
                   );
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

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), null, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(debtPositionTypeOrgs);
      Mockito.when(assessmentsServiceMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);
      Mockito.when(assessmentExtendedDTOMapperMock.mapToPagedAssessmentsExtendedDTO(pagedAssessmentsView, debtPositionTypeOrgMap)).thenReturn(pagedAssessmentsExtendedDTO);

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

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), null, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(null);
      Executable executable = () -> assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
              filters,
              null,
              Pageable.ofSize(1),
              loggedUser,
              accessToken);

      Assertions.assertThrows(ResourceNotFoundException.class, executable);
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

      Executable executable = () -> assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
              filters,
              null,
              Pageable.ofSize(1),
              loggedUser,
              accessToken);

      Assertions.assertThrows(SecurityException.class, executable);
    }
  }

  @Test
  void givenDebtPositionTypeOrgsWithNullEmbeddedWhenGetPagedAssessmentsExtendedDTOThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
            .organizationId(1L)
            .build();

    CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = new CollectionModelDebtPositionTypeOrg();
    debtPositionTypeOrgs.setEmbedded(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(filters.getOrganizationId(), null, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(debtPositionTypeOrgs);

      Pageable pageable = Pageable.ofSize(1);

      Executable executable = () -> assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
              filters,
              null,
              pageable,
              loggedUser,
              accessToken
      );

      Assertions.assertThrows(ResourceNotFoundException.class, executable);

    }
  }

    @Test
    void givenInvalidDebtPositionTypeOrgCodeWhenGetPagedAssessmentsExtendedDTOThenThrowsAuthorizationDeniedException() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setMappedExternalUserId("external-user");

        String code = "INVALID_CODE";
        String accessToken = "token";

        AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
                .organizationId(1L)
                .build();

        try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
            authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

            Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(filters.getOrganizationId(), code, loggedUser.getMappedExternalUserId(), accessToken);
            Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(filters.getOrganizationId(), code, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(null);

            Executable executable = () -> assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(filters, code, Pageable.ofSize(1), loggedUser, accessToken);

            Assertions.assertThrows(ResourceNotFoundException.class, executable);
        }
    }

  @Test
  void givenAssessmentsWithOperatorExternalUserIdWhenGetPagedAssessmentsExtendedDTOThenUserInfoIsAdded() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
      .organizationId(1L)
      .build();

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(123L);
    assessment.setDebtPositionTypeOrgCode("CODE1");
    assessment.setOperatorExternalUserId("operator-123");

    AssessmentsExtendedDTO dto = new AssessmentsExtendedDTO();
    dto.setOperatorExternalUserId("operator-123");
    dto.setAssessmentId(123L);

    PagedAssessmentsView pagedView = PagedAssessmentsView.builder()
      .content(List.of(assessment))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    PagedAssessmentsExtendedDTO pagedDto = PagedAssessmentsExtendedDTO.builder()
      .content(List.of(dto))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("CODE1");
    debtPositionTypeOrg.setDescription("Test Description");

    UserInfo operatorInfo = new UserInfo();
    operatorInfo.setMappedExternalUserId("operator-123");
    operatorInfo.setName("Mario");
    operatorInfo.setFamilyName("Rossi");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(
          filters.getOrganizationId(),
          "CODE1",
          loggedUser.getMappedExternalUserId(),
          accessToken))
        .thenReturn(debtPositionTypeOrg);

      Mockito.when(assessmentsServiceMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken))
        .thenReturn(pagedView);

      Mockito.when(assessmentExtendedDTOMapperMock.mapToPagedAssessmentsExtendedDTO(pagedView, Map.of("CODE1", "Test Description")))
        .thenReturn(pagedDto);

      Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId("operator-123", accessToken))
        .thenReturn(operatorInfo);

      PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
        filters, "CODE1", Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertNotNull(result.getContent());
      Assertions.assertEquals(1, result.getContent().size());
      AssessmentsExtendedDTO resultDto = result.getContent().getFirst();
      Assertions.assertEquals("Mario", resultDto.getName());
      Assertions.assertEquals("Rossi", resultDto.getFamilyName());
    }
  }

  @Test
  void givenAuthzServiceThrowsExceptionWhenGetPagedAssessmentsExtendedDTOThenLogsWarningAndSkipsUserInfo() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
      .organizationId(1L)
      .build();

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(123L);
    assessment.setDebtPositionTypeOrgCode("CODE1");
    assessment.setOperatorExternalUserId("operator-123");

    AssessmentsExtendedDTO dto = new AssessmentsExtendedDTO();
    dto.setOperatorExternalUserId("operator-123");
    dto.setAssessmentId(123L);

    PagedAssessmentsView pagedView = PagedAssessmentsView.builder()
      .content(List.of(assessment))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    PagedAssessmentsExtendedDTO pagedDto = PagedAssessmentsExtendedDTO.builder()
      .content(List.of(dto))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("CODE1");
    debtPositionTypeOrg.setDescription("Test Description");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(
          filters.getOrganizationId(),
          "CODE1",
          loggedUser.getMappedExternalUserId(),
          accessToken))
        .thenReturn(debtPositionTypeOrg);

      Mockito.when(assessmentsServiceMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken))
        .thenReturn(pagedView);

      Mockito.when(assessmentExtendedDTOMapperMock.mapToPagedAssessmentsExtendedDTO(
          pagedView, Map.of("CODE1", "Test Description")))
        .thenReturn(pagedDto);

      Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId("operator-123", accessToken))
        .thenThrow(new RuntimeException("Simulated failure"));

      PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
        filters, "CODE1", Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(1, result.getContent().size());
      AssessmentsExtendedDTO resultDto = result.getContent().getFirst();

      Assertions.assertNull(resultDto.getName());
      Assertions.assertNull(resultDto.getFamilyName());

      Mockito.verify(authzServiceMock, Mockito.times(1))
        .getUserInfoFromMappedExternaUserId("operator-123", accessToken);
    }
  }

  @Test
  void givenAuthzServiceReturnsNullWhenGetPagedAssessmentsExtendedDTOThenUserInfoNotSet() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsFiltersDTO filters = AssessmentsFiltersDTO.builder()
      .organizationId(1L)
      .build();

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(123L);
    assessment.setDebtPositionTypeOrgCode("CODE1");
    assessment.setOperatorExternalUserId("operator-123");

    AssessmentsExtendedDTO dto = new AssessmentsExtendedDTO();
    dto.setOperatorExternalUserId("operator-123");
    dto.setAssessmentId(123L);

    PagedAssessmentsView pagedView = PagedAssessmentsView.builder()
      .content(List.of(assessment))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    PagedAssessmentsExtendedDTO pagedDto = PagedAssessmentsExtendedDTO.builder()
      .content(List.of(dto))
      .totalElements(1L)
      .totalPages(1L)
      .size(1L)
      .number(0)
      .build();

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("CODE1");
    debtPositionTypeOrg.setDescription("Test Description");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(
          filters.getOrganizationId(),
          "CODE1",
          loggedUser.getMappedExternalUserId(),
          accessToken))
        .thenReturn(debtPositionTypeOrg);

      Mockito.when(assessmentsServiceMock.findPagedAssessmentsView(filters, Pageable.ofSize(1), accessToken))
        .thenReturn(pagedView);

      Mockito.when(assessmentExtendedDTOMapperMock.mapToPagedAssessmentsExtendedDTO(
          pagedView, Map.of("CODE1", "Test Description")))
        .thenReturn(pagedDto);

      Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId("operator-123", accessToken))
        .thenReturn(null);

      PagedAssessmentsExtendedDTO result = assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
        filters, "CODE1", Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(1, result.getContent().size());
      AssessmentsExtendedDTO resultDto = result.getContent().getFirst();

      Assertions.assertNull(resultDto.getName());
      Assertions.assertNull(resultDto.getFamilyName());

      Mockito.verify(authzServiceMock, Mockito.times(1))
        .getUserInfoFromMappedExternaUserId("operator-123", accessToken);
    }
  }

  @Test
  void givenAuthzServiceThrowsExceptionWhenEnrichUserInfoByMappedExternalUserIdThenLogsWarningAndSkipsConsumer() throws Exception {
    String mappedExternalUserId = "operator-001";
    String accessToken = "accessToken";

    Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId, accessToken))
      .thenThrow(new RuntimeException("Simulated failure"));

    @SuppressWarnings("unchecked")
    Consumer<UserInfo> consumerMock = Mockito.mock(Consumer.class);

    Method method = AssessmentsRetrieverServiceImpl.class.getDeclaredMethod(
      "enrichUserInfoByMappedExternalUserId",
      String.class,
      String.class,
      Consumer.class
    );
    method.setAccessible(true);

    method.invoke(assessmentsRetrieverService, mappedExternalUserId, accessToken, consumerMock);

    Mockito.verify(authzServiceMock, Mockito.times(1))
      .getUserInfoFromMappedExternaUserId(mappedExternalUserId, accessToken);
    Mockito.verifyNoInteractions(consumerMock);
  }

  @Test
  void givenFiltersWhenGetPagedModelAssessmentsDetailThenPagedAssessmentsRowsDetail() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(PagedModelAssessmentsDetail.class);
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    AssessmentsRowsDetail expectedResult = podamFactory.manufacturePojo(AssessmentsRowsDetail.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(assessmentsRowsDetailFiltersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentsRowsDetailFiltersDTO.getAssessmentId(),accessToken)).thenReturn(assessments);
      Mockito.when(assessmentsServiceMock.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken)).thenReturn(pagedModelAssessmentsDetail);
      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(assessmentsRowsDetailFiltersDTO.getOrganizationId(),assessments.getDebtPositionTypeOrgCode(),loggedUser.getMappedExternalUserId(),accessToken)).thenReturn(debtPositionTypeOrg);
      Mockito.when(assessmentsRowsDetailMapperMock.map(pagedModelAssessmentsDetail,assessments,debtPositionTypeOrg.getDescription())).thenReturn(expectedResult);

      AssessmentsRowsDetail result = assessmentsRetrieverService.getPagedAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
    }
  }

  @Test
  void givenNoDebtPositionWhenGetPagedModelAssessmentsDetailThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    Pageable pageable = Pageable.ofSize(1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(assessmentsRowsDetailFiltersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentsRowsDetailFiltersDTO.getAssessmentId(),accessToken)).thenReturn(assessments);
      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(assessmentsRowsDetailFiltersDTO.getOrganizationId(),assessments.getDebtPositionTypeOrgCode(),loggedUser.getMappedExternalUserId(),accessToken)).thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()->assessmentsRetrieverService.getPagedAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO, pageable, loggedUser, accessToken));

      Mockito.verifyNoInteractions(assessmentsRowsDetailMapperMock);
    }
  }

  @Test
  void givenNoAssessmentsWhenGetPagedModelAssessmentsDetailThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    Pageable pageable = Pageable.ofSize(1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(assessmentsRowsDetailFiltersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentsRowsDetailFiltersDTO.getAssessmentId(),accessToken)).thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()->assessmentsRetrieverService.getPagedAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO, pageable, loggedUser, accessToken));

      Mockito.verifyNoInteractions(assessmentsRowsDetailMapperMock);
    }
  }

  @Test
  void givenInvalidUserWhenPagedModelAssessmentsDetailThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    AssessmentsRowsDetailFiltersDTO filters = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser))
        .thenThrow(new SecurityException("Unauthorized"));

      Executable executable = () -> assessmentsRetrieverService.getPagedAssessmentsRowsDetail(
        filters,
        Pageable.ofSize(1),
        loggedUser,
        accessToken);

      Assertions.assertThrows(SecurityException.class, executable);
    }
  }

  @Test
  void givenValidUserInfoWhenGetPagedAssessmentsRowsDetailThenEnrichesNameAndFamilyName() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    String accessToken = "accessToken";

    AssessmentsRowsDetailFiltersDTO filtersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);

    Assessments assessments = new Assessments();
    assessments.setAssessmentId(filtersDTO.getAssessmentId() != null ? filtersDTO.getAssessmentId() : 123L);
    assessments.setDebtPositionTypeOrgCode("TYPE1");
    assessments.setUpdateOperatorExternalId("operator-001");

    String debtDescription = "debtDescription";
    PagedModelAssessmentsDetail pagedModel = new PagedModelAssessmentsDetail();

    AssessmentsRowsDetail mappedDetail = new AssessmentsRowsDetail();
    mappedDetail.setUpdateOperatorExternalId("operator-001");

    UserInfo operatorInfo = new UserInfo();
    operatorInfo.setMappedExternalUserId("operator-001");
    operatorInfo.setName("Mario");
    operatorInfo.setFamilyName("Rossi");

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("TYPE1");
    debtPositionTypeOrg.setDescription(debtDescription);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() ->
        AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessments.getAssessmentId(), accessToken))
        .thenReturn(assessments);
      Mockito.when(assessmentsServiceMock.findPagedModelAssessmentsDetail(filtersDTO, Pageable.ofSize(1), accessToken))
        .thenReturn(pagedModel);
      Mockito.when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrg(
          filtersDTO.getOrganizationId(),
          assessments.getDebtPositionTypeOrgCode(),
          loggedUser.getMappedExternalUserId(),
          accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(assessmentsRowsDetailMapperMock.map(pagedModel, assessments, debtDescription))
        .thenReturn(mappedDetail);
      Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId("operator-001", accessToken))
        .thenReturn(operatorInfo);

      AssessmentsRowsDetail result = assessmentsRetrieverService.getPagedAssessmentsRowsDetail(
        filtersDTO, Pageable.ofSize(1), loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals("Mario", result.getName());
      Assertions.assertEquals("Rossi", result.getFamilyName());

      Mockito.verify(authzServiceMock, Mockito.times(1))
        .getUserInfoFromMappedExternaUserId("operator-001", accessToken);
    }
  }

  @Test
  void givenIdWhenGetAssessmentsDetailThenReturnAssessmentsDetail() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 1L;
    Long assessmentDetailId = 1L;

    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);
    assessmentsDetail.assessmentId(assessmentId);
    assessmentsDetail.assessmentDetailId(assessmentDetailId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsServiceMock.findAssessmentsDetail(assessmentDetailId, accessToken)).thenReturn(assessmentsDetail);

      AssessmentsDetail result = assessmentsRetrieverService.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId, loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(assessmentsDetail, result);
    }

  }

  @Test
  void givenNullWhenGetAssessmentsDetailThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 1L;
    Long assessmentDetailId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsServiceMock.findAssessmentsDetail(assessmentDetailId, accessToken)).thenReturn(null);

      Executable executable = () -> assessmentsRetrieverService.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId, loggedUser, accessToken);

      InvalidAssessmentsDetailException ex = Assertions.assertThrows(InvalidAssessmentsDetailException.class, executable);
      Assertions.assertEquals("The assessment detail with ID 1 is either invalid or does not belong to the assessment with ID 1", ex.getMessage());
    }

  }

  @Test
  void givenUnrelatedIdWhenGetAssessmentsDetailThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 1L;
    Long assessmentDetailId = 1L;

    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsServiceMock.findAssessmentsDetail(assessmentDetailId, accessToken)).thenReturn(assessmentsDetail);

      Executable executable = () -> assessmentsRetrieverService.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId, loggedUser, accessToken);

      InvalidAssessmentsDetailException ex = Assertions.assertThrows(InvalidAssessmentsDetailException.class, executable);
      Assertions.assertEquals("The assessment detail with ID 1 is either invalid or does not belong to the assessment with ID 1", ex.getMessage());
    }

  }

  @Test
  void givenParamWhenCreateAssessmentThenReturnAssessment() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    String assessmentsName = "assessmentsName";
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(assessmentsServiceMock.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode, accessToken)).thenReturn(assessments);

      Assessments result = assessmentsRetrieverService.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode, loggedUser, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(assessments, result);
    }
  }

  @Test
  void givenInvalidUserWhenCreateAssessmentThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    String assessmentsName = "assessmentsName";
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new SecurityException("Unauthorized"));

      Executable executable = () -> assessmentsRetrieverService.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode, loggedUser, accessToken);

      Assertions.assertThrows(SecurityException.class, executable);
    }
  }

  @Test
  void givenInvalidDebtPositionTypeOrgCodeWhenCreateAssessmentThenThrowsAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    String assessmentsName = "assessmentsName";
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.doThrow(ResourceNotFoundException.class).when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId, debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);

      Executable executable = () -> assessmentsRetrieverService.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode, loggedUser, accessToken);

      Assertions.assertThrows(ResourceNotFoundException.class, executable);
    }
  }

  @Test
  void whenUpdateAssessmentsStatusThenOk(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setAssessmentId(assessmentId);
    assessments.setOrganizationId(organizationId);
    assessments.setFlagManualGeneration(true);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> true);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(assessments);
      Mockito.doNothing().when(assessmentsServiceMock).updateStatus(organizationId, assessmentId, status, accessToken);

      assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken);
      Mockito.verifyNoMoreInteractions(assessmentsServiceMock);
    }
  }

  @Test
  void givenNoAdminRoleWhenUpdateAssessmentsStatusThenOk(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setAssessmentId(assessmentId);
    assessments.setOrganizationId(organizationId);
    assessments.setFlagManualGeneration(true);
    assessments.setOperatorExternalUserId(loggedUser.getMappedExternalUserId());

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> false);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(assessments);
      Mockito.doNothing().when(assessmentsServiceMock).updateStatus(organizationId, assessmentId, status, accessToken);

      assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken);
      Mockito.verifyNoMoreInteractions(assessmentsServiceMock);
    }
  }

  @Test
  void givenNoAdminRoleAndWrongOperatorWhenUpdateAssessmentsStatusThenAuthorizationDeniedException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setAssessmentId(assessmentId);
    assessments.setOrganizationId(organizationId);
    assessments.setFlagManualGeneration(true);
    assessments.setOperatorExternalUserId(loggedUser.getMappedExternalUserId()+1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> false);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(assessments);

      Assertions.assertThrows(AuthorizationDeniedException.class, ()->assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken));
    }
  }

  @Test
  void givenFalseFlagManualGenerationWhenUpdateAssessmentsStatusThenIllegalArgumentException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class );
    assessments.setAssessmentId(assessmentId);
    assessments.setOrganizationId(organizationId);
    assessments.setFlagManualGeneration(false);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> true);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(assessments);

      Assertions.assertThrows(IllegalArgumentException.class,()->assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken));
    }
  }

  @Test
  void givenWrongAssessmentsOrganizationIdWhenUpdateAssessmentsStatusThenResourceNotFoundException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class );
    assessments.setAssessmentId(assessmentId);
    assessments.setOrganizationId(organizationId+1);
    assessments.setFlagManualGeneration(true);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> true);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(assessments);

      Assertions.assertThrows(ResourceNotFoundException.class,()->assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken));
    }
  }

  @Test
  void givenNoAssessmentsWhenUpdateAssessmentsStatusThenResourceNotFoundException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("external-id");
    String accessToken = "accessToken";

    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser)).thenAnswer(a -> true);
      Mockito.when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken)).thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()->assessmentsRetrieverService.updateAssessmentsStatus(organizationId,assessmentId,status, loggedUser, accessToken));
    }
  }
}
