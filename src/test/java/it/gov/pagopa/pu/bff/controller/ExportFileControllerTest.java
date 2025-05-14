package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.export_flow_file.ExportFileRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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

@ExtendWith(MockitoExtension.class)
class ExportFileControllerTest {

  @Mock
  private ExportFileRetrieverService exportFileRetrieverServiceMock;

  @InjectMocks
  private ExportFileController exportFileController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      exportFileRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetExportFilesThenOk() {
    long organizationId = 1L;
    ExportFileTypeEnum exportFileType = ExportFileTypeEnum.CLASSIFICATIONS;
    OffsetDateTime creationDateFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime creationDateTo = OffsetDateTime.now().plusDays(10);
    ExportFileStatus status = ExportFileStatus.COMPLETED;
    String fileName = "filename";
    ExportFileFiltersDTO expectedFilter = new ExportFileFiltersDTO(
      organizationId, exportFileType, new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo), status,
      fileName);
    PagedExportFile expectedResult = new PagedExportFile();
    expectedResult.setContent(List.of(ExportFile.builder()
      .exportFileId(1L)
      .fileName("fileName")
      .creationDate(OffsetDateTime.now())
      .operator("operator")
      .totalRows(10L)
      .status(ExportFileStatus.COMPLETED)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(exportFileRetrieverServiceMock.getExportFiles(
        Mockito.eq(expectedFilter),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<PagedExportFile> response = exportFileController.getExportFiles(organizationId,
      exportFileType,creationDateFrom,creationDateTo,status,fileName,
      PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }

  @Test
  void givenCorrectRequestWhenCreatePaidExportFileThenOk() {
    PaidExportFileRequestDTO requestDTO = PaidExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ExportFileTypeEnum.PAID)
      .fileVersion("version1")
      .paymentDate(new LocalDateIntervalFilter(LocalDate.now(), LocalDate.now()))
      .debtPositionTypeOrgId(1L)
      .build();

    ResponseEntity<Void> response = exportFileController.createPaidExportFile(requestDTO);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(exportFileRetrieverServiceMock)
      .createPaidExportFile(Mockito.eq(requestDTO), Mockito.same(loggedUser), Mockito.same(accessToken));
  }

  @Test
  void givenCorrectRequestWhenCreateClassificationsExportFileThenOk() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(
        ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .build())
      .build();

    ResponseEntity<Void> response = exportFileController.createClassificationsExportFile(requestDTO);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(exportFileRetrieverServiceMock)
      .createClassificationsExportFile(Mockito.eq(requestDTO), Mockito.same(loggedUser), Mockito.same(accessToken));
  }

  @Test
  void givenCorrectRequestWhenCreatePaymentsReportingExportFileThenOk() {
    PaymentsReportingExportFileRequestDTO requestDTO = PaymentsReportingExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(
        PaymentsReportingExportFileRequestDTO.ExportFileTypeEnum.PAYMENTS_REPORTING)
      .fileVersion("version1")
      .filterFields(PaymentsReportingExportFileFilter.builder()
        .build())
      .build();

    ResponseEntity<Void> response = exportFileController.createPaymentsReportingExportFile(requestDTO);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(exportFileRetrieverServiceMock)
      .createPaymentsReportingExportFile(Mockito.eq(requestDTO), Mockito.same(loggedUser), Mockito.same(accessToken));
  }

  @Test
  void givenCorrectRequestWhenCreateArchivingExportFileThenOk() {
    ReceiptsArchivingExportFileRequestDTO receiptsArchivingExportFileRequestDTO = ReceiptsArchivingExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ExportFileTypeEnum.RECEIPTS_ARCHIVING)
      .fileVersion("V1_0")
      .paymentDate(new LocalDateIntervalFilter(LocalDate.now(), LocalDate.now()))
      .build();

    ResponseEntity<Void> response = exportFileController.createReceiptsArchivingExportFile(receiptsArchivingExportFileRequestDTO);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(exportFileRetrieverServiceMock)
      .createReceiptsArchivingExportFile(Mockito.eq(receiptsArchivingExportFileRequestDTO), Mockito.same(loggedUser), Mockito.same(accessToken));
  }

}

