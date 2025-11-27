package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.TransfersApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.transfer.TransferRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TransferController implements TransfersApi {

  private final TransferRetrieverService transferRetrieverService;

  public TransferController(TransferRetrieverService transferRetrieverService) {
    this.transferRetrieverService = transferRetrieverService;
  }

  @Override
  public ResponseEntity<List<Transfer>> getTransfers(Long organizationId, Long installmentId) {
    log.info("User requested getTransfers having organizationId {} and installmentId {}", organizationId, installmentId);
    return ResponseEntity.ok(transferRetrieverService.getTransfers(
      organizationId, installmentId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Boolean> validateTaxonomyCategory(String taxonomyCategory, String orgFiscalCode) {
    log.info("User requested validateTaxonomyCategory on category {} and orgFiscalCode", taxonomyCategory);
    return ResponseEntity.ok(transferRetrieverService.validateTaxonomyCategory(taxonomyCategory, orgFiscalCode, SecurityUtils.getAccessToken()));
  }

}
