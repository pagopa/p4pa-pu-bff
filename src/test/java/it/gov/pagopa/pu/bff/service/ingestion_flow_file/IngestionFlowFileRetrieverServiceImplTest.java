package it.gov.pagopa.pu.bff.service.ingestion_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.IngestionFlowFileService;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.mapper.IngestionFlowFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class IngestionFlowFileRetrieverServiceImplTest {
  @Mock
  private IngestionFlowFileService ingestionFlowFileServiceMock;
  @Mock
  private IngestionFlowFileMapper ingestionFlowFileMapperMock;

  private IngestionFlowFileRetrieverService ingestionFlowFileRetrieverService;

  @BeforeEach
  void setUp() {
    ingestionFlowFileRetrieverService = new IngestionFlowFileRetrieverServiceImpl(ingestionFlowFileServiceMock,ingestionFlowFileMapperMock);
  }

  @Test
  void givenAdminUserWhenGetIngestionFlowFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    List<FlowFileTypeEnum> flowFileTypes = List.of(FlowFileTypeEnum.TREASURY_OPI);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    UserInfo userInfo = new UserInfo();
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(true);
      Mockito.when(ingestionFlowFileServiceMock.getIngestionFlowFiles(
          ingestionFlowFileFilters, null, null, accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(
          pagedModelIngestionFlowFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileRetrieverService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileServiceMock,
        ingestionFlowFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetIngestionFlowFilesThenOk(){
    String accessToken="ACCESSTOKEN";
    long organizationId = 1L;
    List<FlowFileTypeEnum> flowFileTypes = List.of(FlowFileTypeEnum.TREASURY_OPI);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(false);
      Mockito.when(ingestionFlowFileServiceMock.getIngestionFlowFiles(ingestionFlowFileFilters,operatorExternalId,null,accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(pagedModelIngestionFlowFile,userInfo,accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileRetrieverService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult,result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileServiceMock,ingestionFlowFileMapperMock);
    }
  }
}
