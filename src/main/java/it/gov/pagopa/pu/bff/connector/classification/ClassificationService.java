package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.springframework.data.domain.Pageable;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;

public interface ClassificationService {
  PagedTreasuredClassification getTreasuredClassifications(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, Pageable pageable, String accessToken);
  ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken);
}
