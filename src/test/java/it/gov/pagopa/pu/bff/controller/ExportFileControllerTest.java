package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.service.export_flow_file.ExportFileService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum;
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
class ExportFileControllerTest {

  @Mock
  private ExportFileService exportFileServiceMock;

  @InjectMocks
  private ExportFileController exportFileController;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetExportFilesThenOk() {
    long organizationId = 1L;
    FlowFileTypeEnum flowFileType = FlowFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    StatusEnum status = StatusEnum.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO expectedFilter = new ExportFileFiltersDTO(
      organizationId, flowFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    PagedExportFile expectedResult = new PagedExportFile();
    expectedResult.setContent(List.of(ExportFile.builder()
      .exportFileId(1L)
      .fileName("fileName")
      .creationDate(OffsetDateTime.now())
      .operator("operator")
      .totalRows(10L)
      .status(StatusEnum.COMPLETED)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(exportFileServiceMock.getExportFiles(
        Mockito.eq(expectedFilter),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.any(), Mockito.anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedExportFile> response = exportFileController.getExportFiles(organizationId,
      flowFileType,creationDateFrom,creationDateTo,status,fileName,
      PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }
}

