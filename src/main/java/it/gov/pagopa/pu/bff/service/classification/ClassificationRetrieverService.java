package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.springframework.data.domain.Pageable;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;

public interface ClassificationRetrieverService {
  PagedTreasuredClassification getTreasuredClassification(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken);
}
