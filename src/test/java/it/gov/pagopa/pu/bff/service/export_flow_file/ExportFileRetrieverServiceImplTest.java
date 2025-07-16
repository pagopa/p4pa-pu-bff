package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.process_executions.ExportFileService;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.mapper.export_file.PaidExportFileRequestDTOMapper;
import it.gov.pagopa.pu.bff.mapper.export_file.ReceiptsArchivingExportFileRequestDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.processexecutions.dto.generated.*;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
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
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExportFileRetrieverServiceImplTest {
  @Mock
  private ExportFileService exportFileServiceMock;
  @Mock
  private ExportFileMapper exportFileMapperMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;

  private final PaidExportFileRequestDTOMapper paidExportFileRequestDTOMapper = new PaidExportFileRequestDTOMapper();
  private final ReceiptsArchivingExportFileRequestDTOMapper receiptsArchivingExportFileRequestDTOMapper = new ReceiptsArchivingExportFileRequestDTOMapper();

  private PodamFactory podamFactory = TestUtils.getPodamFactory();
  private ExportFileRetrieverService exportFileRetrieverService;

  @BeforeEach
  void setUp() {
    exportFileRetrieverService = new ExportFileRetrieverServiceImpl(
      exportFileServiceMock,
      exportFileMapperMock,
      paidExportFileRequestDTOMapper,
      receiptsArchivingExportFileRequestDTOMapper,
      debtPositionTypeOrgRetrieverServiceMock);
  }

  @Test
  void givenAdminUserWhenGetExportFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    UserInfo userInfo = new UserInfo();
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedExportFile expectedResult = new PagedExportFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(true);
      Mockito.when(exportFileServiceMock.getExportFiles(
          exportFileFilters, null, null, accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(
          pagedModelExportFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileRetrieverService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileServiceMock,
        exportFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetExportFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedExportFile expectedResult = new PagedExportFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(false);
      Mockito.when(exportFileServiceMock.getExportFiles(exportFileFilters, operatorExternalId, null, accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(pagedModelExportFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileRetrieverService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileServiceMock,
        exportFileMapperMock);
    }
  }

  @Test
  void givenNoFiltersWhenGetExportFilesThenThrowIllegalArgumentException() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(
      organizationId, null, new OffsetDateTimeIntervalFilter(null, null), null, null
    );
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser)).thenReturn(true);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        exportFileRetrieverService.getExportFiles(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' dates must be set together", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(exportFileMapperMock);
  }

  @Test
  void givenOnlyCreationDateFromWhenGetExportFilesThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter creationDate = new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(2), null);
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, creationDate, null, null);
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenOnlyCreationDateToWhenGetExportFilesThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter creationDate = new OffsetDateTimeIntervalFilter(null, OffsetDateTime.now());
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, creationDate, null, null);
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenEmptyCreationDateIntervalWhenGetExportFilesThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter creationDate = new OffsetDateTimeIntervalFilter(null, null);
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, creationDate, null, null);
    assertThrowsIllegalArgument(filtersDTO);
  }

  private void assertThrowsIllegalArgument(ExportFileFiltersDTO filtersDTO) {
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser)).thenReturn(true);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        exportFileRetrieverService.getExportFiles(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' dates must be set together", exception.getMessage());
    }

    Mockito.verifyNoInteractions(exportFileMapperMock);
  }

  @Test
  void givenValidCreationDateRangeWhenGetExportFilesThenOk() {
    OffsetDateTimeIntervalFilter creationDate = new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(2), OffsetDateTime.now());
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, creationDate, null, null);
    testSingleExportFileFilterSuccess(filtersDTO);
  }

  @Test
  void givenStatusOnlyWhenGetExportFilesThenOk() {
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, null, ExportFileStatus.COMPLETED, null);
    testSingleExportFileFilterSuccess(filtersDTO);
  }

  @Test
  void givenFileNameOnlyWhenGetExportFilesThenOk() {
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO(1L, null, null, null, "report_2025.csv");
    testSingleExportFileFilterSuccess(filtersDTO);
  }

  private void testSingleExportFileFilterSuccess(ExportFileFiltersDTO filtersDTO) {
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedExportFile expectedPagedExportFile = new PagedExportFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser)).thenReturn(true);

      Mockito.when(exportFileServiceMock.getExportFiles(filtersDTO, null, pageable, accessToken))
        .thenReturn(pagedModelExportFile);

      Mockito.when(exportFileMapperMock.mapToPagedExportFile(pagedModelExportFile, loggedUser, accessToken))
        .thenReturn(expectedPagedExportFile);

      PagedExportFile result = exportFileRetrieverService.getExportFiles(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedExportFile, result);
    }
  }

  @Test
  void whenCreatePaidExportFileThenOk() {
    PaidExportFileRequest requestDTO = PaidExportFileRequest.builder()
      .organizationId(1L)
      .exportFileType(PaidExportFileRequestDTO.ExportFileTypeEnum.PAID)
      .fileVersion("version1")
      .filterFields(new PaidExportFileRequestFilterFields())
      .build();
    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createPaidExportFile(requestDTO, user, accessToken);

    Mockito.verify(exportFileServiceMock).createPaidExportFile(paidExportFileRequestDTOMapper.map2ProcessExecutionsDto(requestDTO), accessToken);
  }

  @Test
  void whenCreateClassificationsExportFileThenOk() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createClassificationsExportFile(requestDTO, user, accessToken);

    Mockito.verify(exportFileServiceMock).createClassificationsExportFile(requestDTO, accessToken);
  }

  @Test
  void givenPopulatedDebtPositionTypeOrgCodesWhenCreateClassificationsExportFileThenOk() {
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = podamFactory.manufacturePojo(List.class, DebtPositionTypeOrg.class);
    Set<String> codes = debtPositionTypeOrgs.stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .debtPositionTypeOrgCodes(codes)
        .build())
      .build();

    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createClassificationsExportFile(requestDTO, user, accessToken);

    ArgumentCaptor<ClassificationsExportFileRequestDTO> captor = ArgumentCaptor.forClass(ClassificationsExportFileRequestDTO.class);
    Mockito.verify(exportFileServiceMock).createClassificationsExportFile(captor.capture(), Mockito.eq(accessToken));

    ClassificationsExportFileRequestDTO capturedDTO = captor.getValue();
    Set<String> actualCodes = capturedDTO.getFilterFields().getDebtPositionTypeOrgCodes();

    assertNotNull(actualCodes);
    assertEquals(codes, actualCodes);

  }

  @Test
  void givenNullDebtPositionTypeOrgCodesWhenCreateClassificationsExportFileThenOk() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .debtPositionTypeOrgCodes(null)
        .build())
      .build();

    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    List<DebtPositionTypeOrg> debtPositionTypeOrgs = podamFactory.manufacturePojo(List.class, DebtPositionTypeOrg.class);
    Set<String> codes = debtPositionTypeOrgs.stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgCodes(1L, user.getMappedExternalUserId(), accessToken))
      .thenReturn(codes);

    exportFileRetrieverService.createClassificationsExportFile(requestDTO, user, accessToken);

    ArgumentCaptor<ClassificationsExportFileRequestDTO> captor = ArgumentCaptor.forClass(ClassificationsExportFileRequestDTO.class);
    Mockito.verify(exportFileServiceMock).createClassificationsExportFile(captor.capture(), Mockito.eq(accessToken));

    ClassificationsExportFileRequestDTO capturedDTO = captor.getValue();
    Set<String> actualCodes = capturedDTO.getFilterFields().getDebtPositionTypeOrgCodes();

    assertNotNull(actualCodes);
    assertEquals(codes, actualCodes);

  }

  @Test
  void givenEmptyDebtPositionTypeOrgCodesWhenCreateClassificationsExportFileThenOk() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .debtPositionTypeOrgCodes(Set.of())
        .build())
      .build();

    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    List<DebtPositionTypeOrg> debtPositionTypeOrgs = podamFactory.manufacturePojo(List.class, DebtPositionTypeOrg.class);
    Set<String> codes = debtPositionTypeOrgs.stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgCodes(1L, user.getMappedExternalUserId(), accessToken))
      .thenReturn(codes);

    exportFileRetrieverService.createClassificationsExportFile(requestDTO, user, accessToken);

    ArgumentCaptor<ClassificationsExportFileRequestDTO> captor = ArgumentCaptor.forClass(ClassificationsExportFileRequestDTO.class);
    Mockito.verify(exportFileServiceMock).createClassificationsExportFile(captor.capture(), Mockito.eq(accessToken));

    ClassificationsExportFileRequestDTO capturedDTO = captor.getValue();
    Set<String> actualCodes = capturedDTO.getFilterFields().getDebtPositionTypeOrgCodes();

    assertNotNull(actualCodes);
    assertEquals(codes, actualCodes);

  }

  @Test
  void givenUnauthorizedDebtPositionTypeOrgCodeWhenCreateClassificationsExportFileThenThrowException() {
    Set<String> unauthorizedCodes = Set.of("UNAUTHORIZED_CODE");
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .debtPositionTypeOrgCodes(unauthorizedCodes)
        .build())
      .build();

    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    Mockito.doThrow(new ResourceNotFoundException("Code not authorized"))
      .when(debtPositionTypeOrgRetrieverServiceMock)
      .validateOperator(Mockito.eq(1L), Mockito.eq("UNAUTHORIZED_CODE"), Mockito.anyString(), Mockito.eq(accessToken));

    assertThrows(ResourceNotFoundException.class, () -> {
      exportFileRetrieverService.createClassificationsExportFile(requestDTO, user, accessToken);
    });

    Mockito.verify(exportFileServiceMock, Mockito.never()).createClassificationsExportFile(Mockito.any(), Mockito.any());
  }


  @Test
  void whenCreatePaymentsReportingExportFileThenOk() {
    PaymentsReportingExportFileRequestDTO requestDTO = PaymentsReportingExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(PaymentsReportingExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(PaymentsReportingExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createPaymentsReportingExportFile(requestDTO, user, accessToken);

    Mockito.verify(exportFileServiceMock).createPaymentsReportingExportFile(requestDTO, accessToken);
  }

  @Test
  void whenCreateReceiptsArchivingExportFileThenOk() {
    ReceiptsArchivingExportFileRequest requestDTO = ReceiptsArchivingExportFileRequest.builder()
      .organizationId(1L)
      .exportFileType(ReceiptsArchivingExportFileRequestDTO.ExportFileTypeEnum.RECEIPTS_ARCHIVING)
      .fileVersion("V1_0")
      .filterFields(new ReceiptsArchivingExportFileRequestFilterFields())
      .build();

    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createReceiptsArchivingExportFile(
      requestDTO, user, accessToken);

    Mockito.verify(exportFileServiceMock).createReceiptsArchivingExportFile(
      receiptsArchivingExportFileRequestDTOMapper.map2ProcessExecutionsDto(
        requestDTO), accessToken);
  }
}
