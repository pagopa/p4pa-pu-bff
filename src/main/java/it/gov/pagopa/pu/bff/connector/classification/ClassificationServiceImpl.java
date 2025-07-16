package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationClient;
import it.gov.pagopa.pu.bff.dto.ClassificationPaidInstallmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassificationServiceImpl implements ClassificationService {

  private final ClassificationClient classificationClient;

  public ClassificationServiceImpl(ClassificationClient classificationClient) {
    this.classificationClient = classificationClient;
  }

  @Override
  public PagedTreasuredClassification getTreasuredClassifications(
    Long organizationId,
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO,
    Pageable pageable, String accessToken) {
    return classificationClient.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken) {
    return classificationClient.getClassificationDetail(organizationId, classificationId, accessToken);
  }

  @Override
  public PagedClassificationPaidInstallmentsView getPaidInstallments(Long organizationId, ClassificationPaidInstallmentsFiltersDTO filters, Pageable pageable, String accessToken) {
    return classificationClient.getPaidInstallments(organizationId, filters, pageable, accessToken);
  }
}
