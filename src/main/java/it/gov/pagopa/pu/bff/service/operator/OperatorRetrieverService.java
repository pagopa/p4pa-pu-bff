package it.gov.pagopa.pu.bff.service.operator;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface OperatorRetrieverService {
  PagedOrganizationOperator getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable, UserInfo loggedUser, String accessToken);
  OperatorsDetail getOperatorDetails(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  int removeDebtPositionTypeOrgFromOperator(Long organizationId, String mappedExternalUserId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);
  PagedDebtPositionTypeOrgDTO getDebtPositionTypeOrgsNotEnabledForOperator(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  void enableDebtPositionTypeOrgsForOperator(Long organizationId, String operatorExternalUserId, Set<Long> debtPositionTypeOrgIds, UserInfo loggedUser, String accessToken);
}
