package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.ClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.dto.ClassificationPaidInstallmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.springframework.data.domain.Pageable;

public interface ClassificationService {
  PagedTreasuredClassification getTreasuredClassifications(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, Pageable pageable, String accessToken);

  ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken);

  PagedClassificationPaidInstallmentsView getPaidInstallments(Long organizationId, ClassificationPaidInstallmentsFiltersDTO filters, Pageable pageable, String accessToken);

  PagedModelClassification getClassifications(Long organizationId, ClassificationFiltersDTO filters, Pageable pageable, String accessToken);
}
