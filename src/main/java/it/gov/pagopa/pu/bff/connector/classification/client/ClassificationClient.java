package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class ClassificationClient {

  private final ClassificationApisHolder classificationApisHolder;

  public ClassificationClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedTreasuredClassification getTreasuredClassifications(Long organizationId, TreasuredClassificationFiltersDTO filters, Pageable pageable, String accessToken) {
    return classificationApisHolder.getClassificationsApi(accessToken)
      .getTreasuredClassifications(organizationId,
        filters.getLastClassificationDate().getFrom(),
        filters.getLastClassificationDate().getTo(),
        filters.getLabel(),
        filters.getIud(),
        filters.getIuv(),
        filters.getIur(),
        filters.getPayDate().getFrom(),
        filters.getPayDate().getTo(),
        filters.getPaymentDateTime().getFrom(),
        filters.getPaymentDateTime().getTo(),
        filters.getRegulationDate().getFrom(),
        filters.getRegulationDate().getTo(),
        filters.getBillDate().getFrom(),
        filters.getBillDate().getTo(),
        filters.getRegionValueDate().getFrom(),
        filters.getRegionValueDate().getTo(),
        filters.getPspCompanyName(),
        filters.getPspLastName(),
        filters.getIuf(),
        filters.getRegulationUniqueIdentifier(),
        filters.getAccountRegistryCode(),
        filters.getBillAmountCents(),
        filters.getRemittanceInformation(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken) {
    try {
      return classificationApisHolder.getClassificationsApi(accessToken)
        .getClassificationDetail(organizationId, classificationId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("ClassificationDetail with organizationId {} and classificationId {} not found", organizationId, classificationId);
      return null;
    }
  }
}
