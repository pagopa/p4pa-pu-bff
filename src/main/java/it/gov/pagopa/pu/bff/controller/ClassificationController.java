package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ClassificationController implements ClassificationsApi {

  private final ClassificationRetrieverService classificationRetrieverService;

  public ClassificationController(
    ClassificationRetrieverService classificationRetrieverService) {
    this.classificationRetrieverService = classificationRetrieverService;
  }

  @Override
  public ResponseEntity<PagedTreasuredClassification> getTreasuredClassifications(
    Long organizationId, ClassificationsEnum label, String iud, String iuv, String iur,
    LocalDate lastClassificationDateFrom, LocalDate lastClassificationDateTo,
    LocalDate payDateFrom, LocalDate payDateTo, OffsetDateTime paymentDateTimeFrom,
    OffsetDateTime paymentDateTimeTo, LocalDate regulationDateFrom, LocalDate regulationDateTo,
    LocalDate billDateFrom, LocalDate billDateTo, LocalDate regionValueDateFrom,
    LocalDate regionValueDateTo, String pspCompanyName, String pspLastName, String iuf,
    String regulationUniqueIdentifier, String accountRegistryCode, Long billAmountCents,
    String remittanceInformation, String debtorFiscalCode, String debtPositionTypeOrgCode,
    String billYear, String billCode, String documentYear, String documentCode,
    String provisionalAe, String provisionalCode, Pageable pageable) {

    log.info("User requested getTreasuredClassifications having organizationId {}", organizationId);

    LocalDateIntervalFilter lastClassificationDateFilter = new LocalDateIntervalFilter(lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateIntervalFilter payDateTimeFilter = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = TreasuredClassificationFiltersDTO.builder()
      .label(label)
      .iud(iud)
      .iuv(iuv)
      .iur(iur)
      .lastClassificationDate(lastClassificationDateFilter)
      .payDate(payDateTimeFilter)
      .paymentDateTime(paymentDateTimeFilter)
      .regulationDate(regulationDateFilter)
      .billDate(billDateFilter)
      .regionValueDate(regionValueDateFilter)
      .pspCompanyName(pspCompanyName)
      .pspLastName(pspLastName)
      .iuf(iuf)
      .regulationUniqueIdentifier(regulationUniqueIdentifier)
      .accountRegistryCode(accountRegistryCode)
      .billAmountCents(billAmountCents)
      .remittanceInformation(remittanceInformation)
      .debtorFiscalCode(debtorFiscalCode)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .billYear(billYear)
      .billCode(billCode)
      .documentYear(documentYear)
      .documentCode(documentCode)
      .provisionalAe(provisionalAe)
      .provisionalCode(provisionalCode)
      .build();

    return ResponseEntity.ok(classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, pageable,
      SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ClassificationDetailViewDTO> getClassificationDetail(Long organizationId, Long classificationId) {
    log.info("User requested getClassificationDetail having organizationId {} and classificationId {}", organizationId, classificationId);

    return ResponseEntity.ofNullable(classificationRetrieverService.getClassificationDetail(
      organizationId, classificationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
