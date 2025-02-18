package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.service.ingestion_flow_file.IngestionFlowFileRetrieverService;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class IngestionFlowFileControllerTest {

  @Mock
  private IngestionFlowFileRetrieverService ingestionFlowFileRetrieverServiceMock;

  @InjectMocks
  private IngestionFlowFileController ingestionFlowFileController;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetIngestionFlowFilesThenOk() {
    long organizationId = 1L;
    List<FlowFileTypeEnum> flowFileTypes = List.of(FlowFileTypeEnum.TREASURY_OPI,FlowFileTypeEnum.PAYMENTS_REPORTING);
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    IngestionFlowFileFiltersDTO expectedFilter = new IngestionFlowFileFiltersDTO(
      organizationId, flowFileTypes, creationDateFrom, creationDateTo, status,
      fileName);
    PagedIngestionFlowFile expectedResult = new PagedIngestionFlowFile();
    expectedResult.setContent(List.of(IngestionFlowFile.builder()
      .ingestionFlowFileId(1L)
      .fileName("fileName")
      .creationDate(OffsetDateTime.now())
      .operator("operator")
      .totalRows(10L)
      .correctlyImportedRows(8L)
      .discardedRows(2L)
      .status(StatusEnum.COMPLETED)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(ingestionFlowFileRetrieverServiceMock.getIngestionFlowFiles(
        Mockito.eq(expectedFilter),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.any(), Mockito.anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedIngestionFlowFile> response = ingestionFlowFileController.getIngestionFlowFiles(organizationId,
      flowFileTypes,creationDateFrom,creationDateTo,status,fileName,
      PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }
}

