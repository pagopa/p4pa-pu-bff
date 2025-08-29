package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.bff.dto.*;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

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
    OffsetDateTime lastClassificationDateTimeFrom, OffsetDateTime lastClassificationDateTimeTo,
    OffsetDateTime payDateTimeFrom, OffsetDateTime payDateTimeTo, OffsetDateTime paymentDateTimeFrom,
    OffsetDateTime paymentDateTimeTo, OffsetDateTime regulationDateTimeFrom, OffsetDateTime regulationDateTimeTo,
    OffsetDateTime billDateTimeFrom, OffsetDateTime billDateTimeTo, OffsetDateTime regionValueDateTimeFrom,
    OffsetDateTime regionValueDateTimeTo, String pspCompanyName, String pspLastName, String iuf,
    String regulationUniqueIdentifier, String accountRegistryCode, Long billAmountCents,
    String remittanceInformation, String debtorFiscalCode, String debtPositionTypeOrgCode,
    String billYear, String billCode, String documentYear, String documentCode,
    String provisionalAe, String provisionalCode, Pageable pageable) {

    log.info("User requested getTreasuredClassifications having organizationId {}", organizationId);

    LocalDateIntervalFilter lastClassificationDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(lastClassificationDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(lastClassificationDateTimeTo));
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(payDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(payDateTimeTo));
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(regulationDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(regulationDateTimeTo));
    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(billDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(billDateTimeTo));
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(regionValueDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(regionValueDateTimeTo));

    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = TreasuredClassificationFiltersDTO.builder()
      .label(label)
      .iud(iud)
      .iuv(iuv)
      .iur(iur)
      .lastClassificationDate(lastClassificationDateFilter)
      .payDate(payDateFilter)
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
      .billYear(billYear)
      .billCode(billCode)
      .documentYear(documentYear)
      .documentCode(documentCode)
      .provisionalAe(provisionalAe)
      .provisionalCode(provisionalCode)
      .build();

    return ResponseEntity.ok(classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, debtPositionTypeOrgCode, pageable,
      SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ClassificationDetailDTO> getClassificationDetail(Long organizationId, Long classificationId) {
    log.info("User requested getClassificationDetail having organizationId {} and classificationId {}", organizationId, classificationId);

    return ResponseEntity.ofNullable(classificationRetrieverService.getClassificationDetail(
      organizationId, classificationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedClassificationPaidInstallmentsView> getPaidInstallments(
    Long organizationId, String debtPositionTypeOrgCode, String iuv, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo,
    OffsetDateTime receiptCreationDateTimeFrom, OffsetDateTime receiptCreationDateTimeTo, Long assessmentId, Pageable pageable) {

    log.info("User requested getPaidInstallments having organizationId {}", organizationId);

    OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    OffsetDateTimeIntervalFilter receiptCreationDateInterval = new OffsetDateTimeIntervalFilter(receiptCreationDateTimeFrom, receiptCreationDateTimeTo);

    ClassificationPaidInstallmentsFiltersDTO classificationPaidInstallmentsFiltersDTO = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv(iuv)
      .paymentDateTimeIntervalFilter(paymentDateTimeIntervalFilter)
      .receiptCreationDateInterval(receiptCreationDateInterval)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .build();

    return ResponseEntity.ok(classificationRetrieverService.getPaidInstallments(
      organizationId, assessmentId, classificationPaidInstallmentsFiltersDTO, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
