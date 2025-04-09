package it.gov.pagopa.pu.bff.connector.process_executions;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileClient;
import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileSearchClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ExportFileServiceTest {

  @Mock
  private ExportFileClient client;
  @Mock
  private ExportFileSearchClient searchClient;

  private ExportFileService service;

  @BeforeEach
  void setUp() {
    service = new ExportFileServiceImpl(client, searchClient);
  }

  @Test
  void whenGetExportFileByIdThenInvokeClient() {
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO();
    String operatorExternalUserId = "MAPPEDEXTERNALUSERID";
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(searchClient.getExportFiles(Mockito.same(filtersDTO), Mockito.same(operatorExternalUserId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelExportFile result = service.getExportFiles(filtersDTO, operatorExternalUserId, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenCreatePaidExportFileThenInvokeClient() {
    PaidExportFileRequestDTO requestDTO = PaidExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(PaidExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(PaidExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    service.createPaidExportFile(requestDTO, accessToken);

    Mockito.verify(client).createPaidExportFile(requestDTO, accessToken);
  }

  @Test
  void whenCreateClassificationsExportFileThenInvokeClient() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    service.createClassificationsExportFile(requestDTO, accessToken);

    Mockito.verify(client).createClassificationsExportFile(requestDTO, accessToken);
  }

  @Test
  void whenCreatePaymentsReportingExportFileThenInvokeClient() {
    PaymentsReportingExportFileRequestDTO requestDTO = PaymentsReportingExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(PaymentsReportingExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(PaymentsReportingExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    service.createPaymentsReportingExportFile(requestDTO, accessToken);

    Mockito.verify(client).createPaymentsReportingExportFile(requestDTO, accessToken);
  }

}
