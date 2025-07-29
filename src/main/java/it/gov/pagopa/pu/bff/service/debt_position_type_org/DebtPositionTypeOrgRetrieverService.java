package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DebtPositionTypeOrgRetrieverService {
  DebtPositionTypeOrgDTO getDebtPositionTypeOrgById(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);

  List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, Boolean flagActive, UserInfo loggedUser, String accessToken);

  PagedDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Boolean flagActive, Pageable pageable, UserInfo loggedUser, String accessToken);

  void deleteDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);

  PagedDebtPositionTypeOrgOperatorDTO getDebtPositionTypeOrgOperators(Long organizationId, Long debtPositionTypeOrgId, Pageable pageable, UserInfo loggedUser, String accessToken);

  DebtPositionTypeOrg createDebtPositionTypeOrg(Long organizationId, SaveDebtPositionTypeOrgDTO createDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken);

  DebtPositionTypeOrg updateDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgId, SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken);

  void validateOperator(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken);

  Set<String> getDebtPositionTypeOrgCodes(Long organizationId, Boolean flagActive, String mappedExternalUserId, String accessToken);

  void validateIuds(Long organizationId, String debtPositionTypeOrgCode, Set<String> iuds, String accessToken);
  void updateFlagActiveDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgCodeId, boolean flagActive, UserInfo loggedUser, String accessToken);
}
