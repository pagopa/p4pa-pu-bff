package it.gov.pagopa.pu.bff.service.ingestion_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.IngestionFlowFileService;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.mapper.IngestionFlowFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.IngestionFlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IngestionFlowFileRetrieverServiceImplTest {
  @Mock
  private IngestionFlowFileService ingestionFlowFileServiceMock;
  @Mock
  private IngestionFlowFileMapper ingestionFlowFileMapperMock;

  private IngestionFlowFileRetrieverService ingestionFlowFileRetrieverService;

  @BeforeEach
  void setUp() {
    ingestionFlowFileRetrieverService = new IngestionFlowFileRetrieverServiceImpl(ingestionFlowFileServiceMock, ingestionFlowFileMapperMock);
  }

  @Test
  void givenAdminUserWhenGetIngestionFlowFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    List<IngestionFlowFileTypeEnum> ingestionFlowFileTypes = List.of(IngestionFlowFileTypeEnum.TREASURY_OPI);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    IngestionFlowFileStatus status = IngestionFlowFileStatus.COMPLETED;
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, ingestionFlowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    UserInfo userInfo = new UserInfo();
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(true);
      Mockito.when(ingestionFlowFileServiceMock.getIngestionFlowFiles(
          ingestionFlowFileFilters, null, null, accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(
          pagedModelIngestionFlowFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileRetrieverService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileServiceMock,
        ingestionFlowFileMapperMock);
    }
  }

  @Test
  void givenNoAdminUserWhenGetIngestionFlowFilesThenOk() {
    String accessToken = "ACCESSTOKEN";
    long organizationId = 1L;
    List<IngestionFlowFileTypeEnum> ingestionFlowFileTypes = List.of(IngestionFlowFileTypeEnum.TREASURY_OPI);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    IngestionFlowFileStatus status = IngestionFlowFileStatus.COMPLETED;
    String fileName = "filename";
    IngestionFlowFileFiltersDTO ingestionFlowFileFilters = new IngestionFlowFileFiltersDTO(
      organizationId, ingestionFlowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(organizationId, userInfo))
        .thenReturn(false);
      Mockito.when(ingestionFlowFileServiceMock.getIngestionFlowFiles(ingestionFlowFileFilters, operatorExternalId, null, accessToken))
        .thenReturn(pagedModelIngestionFlowFile);
      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(pagedModelIngestionFlowFile, userInfo, accessToken))
        .thenReturn(expectedResult);

      PagedIngestionFlowFile result = ingestionFlowFileRetrieverService.getIngestionFlowFiles(
        ingestionFlowFileFilters, null, userInfo, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(organizationId, userInfo));
      Mockito.verifyNoMoreInteractions(ingestionFlowFileServiceMock, ingestionFlowFileMapperMock);
    }
  }

  @Test
  void givenNoFiltersWhenGetIngestionFlowFilesThenThrowIllegalArgumentException() {
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    IngestionFlowFileFiltersDTO filtersDTO = new IngestionFlowFileFiltersDTO(
      1L, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser)).thenReturn(true);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        ingestionFlowFileRetrieverService.getIngestionFlowFiles(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' dates must be set together", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser));
    }

    Mockito.verifyNoInteractions(ingestionFlowFileMapperMock);
  }

  @Test
  void givenValidCreationDateRangeWhenGetIngestionFlowFilesThenOk() {
    IngestionFlowFileFiltersDTO filtersDTO = new IngestionFlowFileFiltersDTO(
      1L, null, OffsetDateTime.now().minusDays(2), OffsetDateTime.now(), null, null);
    testSingleIngestionFlowFileFilterSuccess(filtersDTO);
  }

  @Test
  void givenStatusOnlyWhenGetIngestionFlowFilesThenOk() {
    IngestionFlowFileFiltersDTO filtersDTO = new IngestionFlowFileFiltersDTO(
      1L, null, null, null, IngestionFlowFileStatus.COMPLETED, null);
    testSingleIngestionFlowFileFilterSuccess(filtersDTO);
  }

  @Test
  void givenFileNameOnlyWhenGetIngestionFlowFilesThenOk() {
    IngestionFlowFileFiltersDTO filtersDTO = new IngestionFlowFileFiltersDTO(
      1L, null, null, null, null, "flow_2025.csv");
    testSingleIngestionFlowFileFilterSuccess(filtersDTO);
  }

  private void testSingleIngestionFlowFileFilterSuccess(IngestionFlowFileFiltersDTO filtersDTO) {
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelIngestionFlowFile pagedModel = new PagedModelIngestionFlowFile();
    PagedIngestionFlowFile expectedPaged = new PagedIngestionFlowFile();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isAdminRole(filtersDTO.getOrganizationId(), loggedUser)).thenReturn(true);

      Mockito.when(ingestionFlowFileServiceMock.getIngestionFlowFiles(filtersDTO, null, pageable, accessToken))
        .thenReturn(pagedModel);

      Mockito.when(ingestionFlowFileMapperMock.mapToPagedIngestionFlowFile(pagedModel, loggedUser, accessToken))
        .thenReturn(expectedPaged);

      PagedIngestionFlowFile result = ingestionFlowFileRetrieverService.getIngestionFlowFiles(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPaged, result);
    }
  }
}
