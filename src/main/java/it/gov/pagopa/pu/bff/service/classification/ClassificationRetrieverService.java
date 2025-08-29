package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.dto.ClassificationPaidInstallmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import org.springframework.data.domain.Pageable;

public interface ClassificationRetrieverService {
  PagedTreasuredClassificationExtendedDTO getTreasuredClassification(Long organizationId, TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken);

  ClassificationDetailDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken);

  PagedClassificationPaidInstallmentsView getPaidInstallments(Long organizationId, Long assessmentId, ClassificationPaidInstallmentsFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken);
}
