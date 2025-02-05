package it.gov.pagopa.pu.bff.connector.process_executions.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.controller.generated.IngestionFlowFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
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
class IngestionFlowFileSearchClientTest {

  @Mock
  private ProcessExecutionsApisHolder processExecutionsApisHolderMock;
  @Mock
  private IngestionFlowFileSearchControllerApi ingestionFlowFileSearchControllerApiMock;

  private IngestionFlowFileSearchClient ingestionFlowFileSearchClient;

  @BeforeEach
  void setUp() {
    ingestionFlowFileSearchClient = new IngestionFlowFileSearchClient(processExecutionsApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(processExecutionsApisHolderMock);
  }

  @Test
  void whenGetIngestionFlowFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelIngestionFlowFile expectedResult = new PagedModelIngestionFlowFile();

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType,creationDateFrom,
      creationDateTo,status,fileName,operatorExternalId,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelIngestionFlowFile result = ingestionFlowFileSearchClient.getIngestionFlowFiles(
      ingestionFlowFileFilters,operatorExternalId, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetIngestionFlowFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    String accessToken = "ACCESSTOKEN";
    PagedModelIngestionFlowFile expectedResult = new PagedModelIngestionFlowFile();

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType,creationDateFrom,
      creationDateTo,status,fileName,operatorExternalId,0,null,Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelIngestionFlowFile result = ingestionFlowFileSearchClient.getIngestionFlowFiles(
      ingestionFlowFileFilters,operatorExternalId, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetIngestionFlowFilesThenThrowIt() {
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType,creationDateFrom,
      creationDateTo,status,fileName,operatorExternalId,0,10,sortList))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> ingestionFlowFileSearchClient.getIngestionFlowFiles(
        ingestionFlowFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetIngestionFlowFilesThenThrowIt() {
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),flowFileType,creationDateFrom,
      creationDateTo,status,fileName,operatorExternalId,0,10,sortList))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> ingestionFlowFileSearchClient.getIngestionFlowFiles(
        ingestionFlowFileFilters, operatorExternalId,PageRequest.of(0,10,
          Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken));

    Assertions.assertSame(expectedException, result);
  }
}

