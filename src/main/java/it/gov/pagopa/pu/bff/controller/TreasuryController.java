package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.TreasuriesApi;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
public class TreasuryController implements TreasuriesApi {

  private final TreasuryRetrieverService treasuryRetrieverService;

  public TreasuryController(TreasuryRetrieverService treasuryRetrieverService) {
    this.treasuryRetrieverService = treasuryRetrieverService;
  }

  @Override
  public ResponseEntity<PagedTreasuryView> getTreasuries(Long organizationId, String iuv, String iuf, Long billAmountCents, LocalDate billDate, String provisionalCode, String billCode, String pspLastName, LocalDate regionValueDate, String documentCode, Pageable pageable) {
    log.info("User requested getTreasuries having organizationId {}", organizationId);

    return ResponseEntity.ok(treasuryRetrieverService.getTreasuries(
      new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode),
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
