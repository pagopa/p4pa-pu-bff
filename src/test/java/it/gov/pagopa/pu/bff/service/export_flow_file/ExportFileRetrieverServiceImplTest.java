package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.process_executions.ExportFileService;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportFileRetrieverServiceImplTest {
  @Mock
  private ExportFileService exportFileServiceMock;
  @Mock
  private ExportFileMapper exportFileMapperMock;

  private ExportFileRetrieverService exportFileRetrieverService;

  @BeforeEach
  void setUp() {
    exportFileRetrieverService = new ExportFileRetrieverServiceImpl(
      exportFileServiceMock, exportFileMapperMock);
  }

  @Test
  void givenAdminUserWhenGetExportFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    ExportFileTypeEnum flowFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    UserInfo userInfo = new UserInfo();
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedExportFile expectedResult = new PagedExportFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(true);
      Mockito.when(exportFileServiceMock.getExportFiles(
          exportFileFilters, null, null, accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(
          pagedModelExportFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileRetrieverService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileServiceMock,
        exportFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetExportFilesThenOk(){
    String accessToken="ACCESSTOKEN";
    long organizationId = 1L;
    ExportFileTypeEnum flowFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedExportFile expectedResult = new PagedExportFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(false);
      Mockito.when(exportFileServiceMock.getExportFiles(exportFileFilters,operatorExternalId,null,accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(pagedModelExportFile,userInfo,accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileRetrieverService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult,result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileServiceMock,
        exportFileMapperMock);
    }
  }

  @Test
  void whenCreateExportFileThenOk() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .flowFileType(ExportFileRequestDTO.FlowFileTypeEnum.CLASSIFICATIONS)
      .flowFileVersion("version1")
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";
    UserInfo user = TestUtils.getSampleUser();

    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("ROLE_USER"));
    userOrgRole.setOrganizationId(1L);
    user.setOrganizations(List.of(userOrgRole));

    exportFileRetrieverService.createExportFile(requestDTO, user, accessToken);

    Mockito.verify(exportFileServiceMock).createExportFile(requestDTO, accessToken);
  }
}
