package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExportFileClient {

  private final ProcessExecutionsApisHolder processExecutionsApisHolder;

  public ExportFileClient(
    ProcessExecutionsApisHolder processExecutionsApisHolder) {
    this.processExecutionsApisHolder = processExecutionsApisHolder;
  }

  public void createPaidExportFile(PaidExportFileRequestDTO paidExportFileRequestDTOrequestDTO, String accessToken) {
    processExecutionsApisHolder.getExportFileControllerApi(accessToken)
      .createPaidExportFile(paidExportFileRequestDTOrequestDTO);
  }

  public void createClassificationsExportFile(
    ClassificationsExportFileRequestDTO classificationsExportFileRequestDTO, String accessToken) {
    processExecutionsApisHolder.getExportFileControllerApi(accessToken)
      .createClassificationsExportFile(classificationsExportFileRequestDTO);
  }

  public void createPaymentsReportingExportFile(
    PaymentsReportingExportFileRequestDTO paymentsReportingExportFileRequestDTO, String accessToken) {
    processExecutionsApisHolder.getExportFileControllerApi(accessToken)
      .createPaymentsReportingExportFile(paymentsReportingExportFileRequestDTO);
  }

  public void createReceiptsArchivingExportFile(ReceiptsArchivingExportFileRequestDTO receiptsArchivingExportFileRequestDTO , String accessToken){
    processExecutionsApisHolder.getExportFileControllerApi(accessToken).createReceiptsArchivingExportFile(receiptsArchivingExportFileRequestDTO);
  }

}
