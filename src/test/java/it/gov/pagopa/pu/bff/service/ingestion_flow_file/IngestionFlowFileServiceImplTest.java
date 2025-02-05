package it.gov.pagopa.pu.bff.service.ingestion_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.client.IngestionFlowFileSearchClient;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.mapper.IngestionFlowFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
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
class IngestionFlowFileServiceImplTest {
  @Mock
  private IngestionFlowFileSearchClient ingestionFlowFileSearchClientMock;
  @Mock
  private IngestionFlowFileMapper ingestionFlowFileMapperMock;

  private IngestionFlowFileService ingestionFlowFileService;

  @BeforeEach
  void setUp() {
    ingestionFlowFileService = new IngestionFlowFileServiceImpl(ingestionFlowFileSearchClientMock,ingestionFlowFileMapperMock);
  }

  @Test
  void givenAdminUserWhenGetIngestionFlowFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    UserInfo userInfo = new UserInfo();
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(true);
      Mockito.when(ingestionFlowFileSearchClientMock.getIngestionFlowFiles(
          ingestionFlowFileFilters, null, null, accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(
          pagedModelIngestionFlowFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileSearchClientMock,
        ingestionFlowFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetIngestionFlowFilesThenOk(){
    String accessToken="ACCESSTOKEN";
    long organizationId = 1L;
    String flowFileType = "flowFileType";
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    String status = "status";
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileType, creationDateFrom, creationDateTo, status,
      fileName);
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(()->AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(false);
      Mockito.when(ingestionFlowFileSearchClientMock.getIngestionFlowFiles(ingestionFlowFileFilters,operatorExternalId,null,accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(pagedModelIngestionFlowFile,userInfo,accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult,result);
      authorizationServiceMockedStatic.verify(()->AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileSearchClientMock,ingestionFlowFileMapperMock);
    }
  }
}
