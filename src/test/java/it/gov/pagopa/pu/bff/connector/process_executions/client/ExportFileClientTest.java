package it.gov.pagopa.pu.bff.connector.process_executions.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileControllerApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ExportFileClientTest {

  @Mock
  private ProcessExecutionsApisHolder processExecutionsApisHolderMock;
  @Mock
  private ExportFileSearchControllerApi exportFileSearchControllerApiMock;
  @Mock
  private ExportFileControllerApi exportFileControllerApiMock;

  private ExportFileClient exportFileClient;

  @BeforeEach
  void setUp() {
    exportFileClient = new ExportFileClient(processExecutionsApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(processExecutionsApisHolderMock);
  }

  @Test
  void whenGetExportFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType.toString(),creationDateFrom,
      creationDateTo,status.name(),fileName,operatorExternalId,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelExportFile result = exportFileClient.getExportFiles(
      exportFileFilters,operatorExternalId, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetExportFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    String accessToken = "ACCESSTOKEN";
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType.toString(),creationDateFrom,
      creationDateTo,status.name(),fileName,operatorExternalId,0,null,Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelExportFile result = exportFileClient.getExportFiles(
      exportFileFilters,operatorExternalId, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetExportFilesThenThrowIt() {
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType.toString(),creationDateFrom,
      creationDateTo,status.name(),fileName,operatorExternalId,0,10,sortList))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileClient.getExportFiles(
        exportFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetExportFilesThenThrowIt() {
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType.toString(),creationDateFrom,
      creationDateTo,status.name(),fileName,operatorExternalId,0,10,sortList))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileClient.getExportFiles(
        exportFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void whenCreateExportFileThenOk() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .flowFileType(ExportFileRequestDTO.FlowFileTypeEnum.CLASSIFICATIONS)
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);

    exportFileClient.createExportFile(requestDTO, accessToken);

    verify(exportFileControllerApiMock).createExportFile(requestDTO);
  }

  @Test
  void givenHttpExceptionWhenCreateExportFileThenThrowIt() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .flowFileType(ExportFileRequestDTO.FlowFileTypeEnum.CLASSIFICATIONS)
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);
    doThrow(expectedException).when(exportFileControllerApiMock).createExportFile(requestDTO);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileClient.createExportFile(requestDTO, accessToken));

    assertSame(expectedException, result);
    verify(exportFileControllerApiMock).createExportFile(requestDTO);
  }

  @Test
  void givenGenericExceptionWhenCreateExportFileThenThrowIt() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .flowFileType(ExportFileRequestDTO.FlowFileTypeEnum.CLASSIFICATIONS)
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);
    doThrow(expectedException).when(exportFileControllerApiMock).createExportFile(requestDTO);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileClient.createExportFile(requestDTO, accessToken));

    assertSame(expectedException, result);
    verify(exportFileControllerApiMock).createExportFile(requestDTO);
  }
}

