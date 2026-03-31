package it.gov.pagopa.pu.bff.connector.process_executions.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
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
class ExportFileSearchClientTest {

  @Mock
  private ProcessExecutionsApisHolder processExecutionsApisHolderMock;
  @Mock
  private ExportFileSearchControllerApi exportFileSearchControllerApiMock;


  private ExportFileSearchClient exportFileSearchClient;

  @BeforeEach
  void setUp() {
    exportFileSearchClient = new ExportFileSearchClient(processExecutionsApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(processExecutionsApisHolderMock);
  }

  @Test
  void whenGetExportFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      organizationId,exportFileType.toString(), DateUtils.toLocalDateTime(creationDateFrom),
      DateUtils.toLocalDateTime(creationDateTo),operatorExternalId, status,fileName,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelExportFile result = exportFileSearchClient.getExportFiles(
      exportFileFilters,operatorExternalId, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetExportFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    String accessToken = "ACCESSTOKEN";
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      organizationId,exportFileType.toString(),DateUtils.toLocalDateTime(creationDateFrom),
      DateUtils.toLocalDateTime(creationDateTo) ,operatorExternalId,status,fileName,0,null,Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelExportFile result = exportFileSearchClient.getExportFiles(
      exportFileFilters,operatorExternalId, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetExportFilesThenThrowIt() {
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      organizationId,exportFileType.toString(),DateUtils.toLocalDateTime(creationDateFrom),
      DateUtils.toLocalDateTime(creationDateTo) ,operatorExternalId,status,fileName,0,10,sortList))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileSearchClient.getExportFiles(
        exportFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetExportFilesThenThrowIt() {
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    ExportFileFiltersDTO exportFileFilters = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(processExecutionsApisHolderMock.getExportFileSearchControllerApi(accessToken))
      .thenReturn(exportFileSearchControllerApiMock);
    when(exportFileSearchControllerApiMock.crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
      organizationId,exportFileType.toString(), DateUtils.toLocalDateTime(creationDateFrom),
      DateUtils.toLocalDateTime(creationDateTo), operatorExternalId,status,fileName,0,10,sortList))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> exportFileSearchClient.getExportFiles(
        exportFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }

}

