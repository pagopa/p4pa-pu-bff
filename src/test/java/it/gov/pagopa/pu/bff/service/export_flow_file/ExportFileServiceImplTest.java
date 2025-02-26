package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileSearchClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportFileServiceImplTest {
  @Mock
  private ExportFileSearchClient exportFileSearchClientMock;
  @Mock
  private ExportFileMapper exportFileMapperMock;

  private ExportFileService exportFileService;

  @BeforeEach
  void setUp() {
    exportFileService = new ExportFileServiceImpl(
      exportFileSearchClientMock, exportFileMapperMock);
  }

  @Test
  void givenAdminUserWhenGetExportFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
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
      Mockito.when(exportFileSearchClientMock.getExportFiles(
          exportFileFilters, null, null, accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(
          pagedModelExportFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileSearchClientMock,
        exportFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetIngestionFlowFilesThenOk(){
    String accessToken="ACCESSTOKEN";
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
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
      Mockito.when(exportFileSearchClientMock.getExportFiles(exportFileFilters,operatorExternalId,null,accessToken))
        .thenReturn(pagedModelExportFile);
      Mockito.when(exportFileMapperMock.mapToPagedExportFile(pagedModelExportFile,userInfo,accessToken))
        .thenReturn(expectedResult);

      PagedExportFile result = exportFileService.getExportFiles(
        exportFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult,result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(exportFileSearchClientMock,
        exportFileMapperMock);
    }
  }
}
