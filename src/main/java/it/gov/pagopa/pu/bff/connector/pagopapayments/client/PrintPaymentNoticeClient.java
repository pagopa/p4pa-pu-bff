package it.gov.pagopa.pu.bff.connector.pagopapayments.client;

import it.gov.pagopa.pu.bff.connector.pagopapayments.config.PagoPAPaymentsApisHolder;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintPaymentNoticeClient {

  private final PagoPAPaymentsApisHolder pagoPAPaymentsApisHolder;

  public PrintPaymentNoticeClient(
    PagoPAPaymentsApisHolder processExecutionsApisHolder) {
    this.pagoPAPaymentsApisHolder = processExecutionsApisHolder;
  }

  public FileResourceDTO generateNotice(
    Long organizationId, String iuv, DebtPositionDTO debtPositionDTO, String accessToken) {
    ResponseEntity<Resource> resourceResponseEntity = pagoPAPaymentsApisHolder.getPrintPaymentNoticeControllerApi(
        accessToken)
      .generateNoticeWithHttpInfo(organizationId, iuv, debtPositionDTO);
    return FileResourceDTO.builder()
      .resource(resourceResponseEntity.getBody())
      .fileName(resourceResponseEntity.getHeaders().getContentDisposition().getFilename())
      .build();
  }

}
