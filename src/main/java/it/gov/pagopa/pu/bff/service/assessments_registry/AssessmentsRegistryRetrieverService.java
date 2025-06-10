package it.gov.pagopa.pu.bff.service.assessments_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import org.springframework.data.domain.Pageable;

public interface AssessmentsRegistryRetrieverService {
    PagedAssessmentsRegistry getAssessmentsRegistries(AssessmentsRegistryFiltersDTO filters, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken);
}
