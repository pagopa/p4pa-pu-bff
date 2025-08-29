package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.TreasuriesApi;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@Slf4j
public class TreasuryController implements TreasuriesApi {

  private final TreasuryRetrieverService treasuryRetrieverService;

  public TreasuryController(TreasuryRetrieverService treasuryRetrieverService) {
    this.treasuryRetrieverService = treasuryRetrieverService;
  }

  @Override
  public ResponseEntity<PagedTreasuryView> getTreasuries(Long organizationId, String iuv, String iuf, Long billAmountCents, OffsetDateTime billDateFrom, OffsetDateTime billDateTo, String provisionalCode, String provisionalAe, String billCode, String billYear, String pspLastName, OffsetDateTime regionValueDateFrom, OffsetDateTime regionValueDateTo, String documentCode, String documentYear, Pageable pageable) {
    log.info("User requested getTreasuries having organizationId {}", organizationId);
    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(billDateFrom), DateUtils.fromOffsetDateTimeToLocalDate(billDateTo));
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(regionValueDateFrom), DateUtils.fromOffsetDateTimeToLocalDate(regionValueDateTo));

    return ResponseEntity.ok(treasuryRetrieverService.getTreasuries(
      new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, provisionalAe, billCode, billYear, pspLastName, regionValueDateFilter, documentCode, documentYear),
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Treasury> getTreasuryDetail(Long organizationId, String treasuryId) {
    log.info("User requested getTreasuryDetail having organizationId {} and treasuryId {}", organizationId, treasuryId);

    return ResponseEntity.ofNullable(treasuryRetrieverService.getTreasuryDetail(
      organizationId, treasuryId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
