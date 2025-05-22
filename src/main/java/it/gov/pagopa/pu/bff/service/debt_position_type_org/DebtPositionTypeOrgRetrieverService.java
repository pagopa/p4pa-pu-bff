package it.gov.pagopa.pu.bff.service.debt_position_type_org;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DebtPositionTypeOrgRetrieverService {
  DebtPositionTypeOrgDTO getDebtPositionTypeOrgById(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);

  List<DebtPositionTypeOrg> getDebtPositionTypeOrgs(Long organizationId, UserInfo loggedUser, String accessToken);

  PagedDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable, UserInfo loggedUser, String accessToken);

  void deleteDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken);

  PagedDebtPositionTypeOrgOperatorDTO getDebtPositionTypeOrgOperators(Long organizationId, Long debtPositionTypeOrgId, Pageable pageable, UserInfo loggedUser, String accessToken);

  DebtPositionTypeOrg createDebtPositionTypeOrg(Long organizationId, SaveDebtPositionTypeOrgDTO createDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken);

  DebtPositionTypeOrg updateDebtPositionTypeOrg(Long organizationId, Long debtPositionTypeOrgId, SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO, UserInfo loggedUser, String accessToken);
}
