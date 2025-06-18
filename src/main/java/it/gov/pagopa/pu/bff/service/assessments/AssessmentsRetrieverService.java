package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.springframework.data.domain.Pageable;

public interface AssessmentsRetrieverService {
  PagedAssessmentsExtendedDTO getPagedAssessmentsExtendedDTO(AssessmentsFiltersDTO assessmentsFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken);
  PagedModelAssessmentsDetail getPagedModelAssessmentsDetail(AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
}
