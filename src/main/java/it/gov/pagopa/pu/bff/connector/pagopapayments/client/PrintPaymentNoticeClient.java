package it.gov.pagopa.pu.bff.connector.pagopapayments.client;

import it.gov.pagopa.pu.bff.connector.pagopapayments.config.PagoPAPaymentsApisHolder;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
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
    String nav, DebtPositionDTO debtPositionDTO, String accessToken) {
    ResponseEntity<Resource> resourceResponseEntity = pagoPAPaymentsApisHolder.getPrintPaymentNoticeControllerApi(
        accessToken)
      .generateNoticeWithHttpInfo(nav, debtPositionDTO);
    return FileResourceDTO.builder()
      .resource(resourceResponseEntity.getBody())
      .fileName(resourceResponseEntity.getHeaders().getContentDisposition().getFilename())
      .build();
  }

}
