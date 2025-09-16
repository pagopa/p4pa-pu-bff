package it.gov.pagopa.pu.bff.service.operator;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import org.springframework.data.domain.Pageable;

public interface OperatorRetrieverService {
  PagedOrganizationOperator getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable, UserInfo loggedUser, String accessToken);
  OperatorsDetail getOperatorDetails(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  int deleteOperator(Long organizationId, String mappedExternalUserId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);
}
