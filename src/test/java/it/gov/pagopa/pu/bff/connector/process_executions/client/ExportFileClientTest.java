package it.gov.pagopa.pu.bff.connector.process_executions.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExportFileClientTest {

  @Mock
  private ProcessExecutionsApisHolder processExecutionsApisHolderMock;
  @Mock
  private ExportFileControllerApi exportFileControllerApiMock;

  private ExportFileClient exportFileClient;

  @BeforeEach
  void setUp() {
    exportFileClient = new ExportFileClient(processExecutionsApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(processExecutionsApisHolderMock);
  }

  @Test
  void whenCreatePaidExportFileThenOk() {
    PaidExportFileRequestDTO requestDTO = PaidExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(PaidExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(PaidExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);

    exportFileClient.createPaidExportFile(requestDTO, accessToken);

    verify(exportFileControllerApiMock).createPaidExportFile(requestDTO);
  }

  @Test
  void whenCreateClassificationsExportFileThenOk() {
    ClassificationsExportFileRequestDTO requestDTO = ClassificationsExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ClassificationsExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ClassificationsExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);

    exportFileClient.createClassificationsExportFile(requestDTO, accessToken);

    verify(exportFileControllerApiMock).createClassificationsExportFile(requestDTO);
  }

  @Test
  void whenCreatePaymentsReportingExportFileThenOk() {
    PaymentsReportingExportFileRequestDTO requestDTO = PaymentsReportingExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(PaymentsReportingExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(PaymentsReportingExportFileFilter.builder()
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);

    exportFileClient.createPaymentsReportingExportFile(requestDTO, accessToken);

    verify(exportFileControllerApiMock).createPaymentsReportingExportFile(requestDTO);
  }
}
