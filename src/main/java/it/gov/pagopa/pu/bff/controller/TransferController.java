package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.TransfersApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.transfer.TransferRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.TransferResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransferController implements TransfersApi {

  private final TransferRetrieverService transferRetrieverService;

  public TransferController(TransferRetrieverService transferRetrieverService) {
    this.transferRetrieverService = transferRetrieverService;
  }

  @Override
  public ResponseEntity<List<TransferResponse>> getTransfers(Long organizationId, Long installmentId) {
    return ResponseEntity.ok(transferRetrieverService.getTransfers(
      organizationId, installmentId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
