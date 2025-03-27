package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.controller.generated.IngestionFlowFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.IngestionFlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import org.junit.jupiter.api.AfterEach;
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

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

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
    Mockito.verifyNoMoreInteractions(
      processExecutionsApisHolderMock,
      ingestionFlowFileSearchControllerApiMock
    );
  }

  @Test
  void whenGetIngestionFlowFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    List<IngestionFlowFileTypeEnum> flowFileTypes = List.of(IngestionFlowFileTypeEnum.TREASURY_OPI,IngestionFlowFileTypeEnum.PAYMENTS_REPORTING);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    IngestionFlowFileStatus status = IngestionFlowFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    List<String> sortList = List.of("sort1,ASC","sort2,DESC");
    String accessToken = "ACCESSTOKEN";
    PagedModelIngestionFlowFile expectedResult = new PagedModelIngestionFlowFile();

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),List.of(IngestionFlowFileTypeEnum.TREASURY_OPI.toString(),IngestionFlowFileTypeEnum.PAYMENTS_REPORTING.toString()),creationDateFrom.toLocalDateTime(),
      creationDateTo.toLocalDateTime(),status,fileName,operatorExternalId,0,10,sortList))
      .thenReturn(expectedResult);

    PagedModelIngestionFlowFile result = ingestionFlowFileSearchClient.getIngestionFlowFiles(
      ingestionFlowFileFilters,operatorExternalId, PageRequest.of(0,10,
        Sort.by(List.of(Order.asc("sort1"),Order.desc("sort2")))), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnpagedWhenGetIngestionFlowFilesThenInvokeWithAccessToken() {
    long organizationId = 1L;
    IngestionFlowFileTypeEnum flowFileType = IngestionFlowFileTypeEnum.TREASURY_OPI;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    IngestionFlowFileStatus status = IngestionFlowFileStatus.COMPLETED;
    String fileName = "filename";
    String operatorExternalId = "operatorExternalId";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, List.of(flowFileType), creationDateFrom, creationDateTo, status,
      fileName);
    String accessToken = "ACCESSTOKEN";
    PagedModelIngestionFlowFile expectedResult = new PagedModelIngestionFlowFile();

    when(processExecutionsApisHolderMock.getIngestionFlowFileSearchControllerApi(accessToken))
      .thenReturn(ingestionFlowFileSearchControllerApiMock);
    when(ingestionFlowFileSearchControllerApiMock.crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
      String.valueOf(organizationId),List.of(flowFileType.toString()),creationDateFrom.toLocalDateTime(),
      creationDateTo.toLocalDateTime(),status,fileName,operatorExternalId,0,null,Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelIngestionFlowFile result = ingestionFlowFileSearchClient.getIngestionFlowFiles(
      ingestionFlowFileFilters,operatorExternalId, Pageable.unpaged(), accessToken);

    assertSame(expectedResult, result);
  }

}

