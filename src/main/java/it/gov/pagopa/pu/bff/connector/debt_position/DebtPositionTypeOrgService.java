package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface DebtPositionTypeOrgService {
  CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, Boolean flagActive, String operatorExternalUserId, String accessToken);

  DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken);

  PagedModelDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Boolean flagActive, Pageable pageable, String accessToken);

  CollectionModelDebtPositionTypeOrgCountByOrganizationId getDebtPositionTypeOrgCountByOrganizationId(List<Long> organizationIds, String accessToken);

  void deleteDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken);

  PagedModelDebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeId(Long debtPositionTypeId, Pageable pageable, String accessToken);

  DebtPositionTypeOrg saveDebtPositionTypeOrg(SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrg, String accessToken);

  DebtPositionTypeOrg findDebtPositionTypeOrg(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken);

  List<DebtPositionTypeOrg> findDebtPositionTypeOrgByOrganizationIdAndIuds(Long organizationId, Set<String> iuds, String accessToken);

  void updateFlagActiveDebtPositionTypeOrg(Long debtPositionTypeOrgId, Boolean flagActive, String accessToken);

  Long countByOrgSilServiceId(Long orgSilServiceId, String accessToken);

  PagedModelDebtPositionTypeOrg findPagedDebtPositionTypeOrg(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, String accessToken);

  CollectionModelDebtPositionTypeOrg getByDebtPositionTypeOrgIdIn(Set<Long> debtPositionTypeOrgIds, String accessToken);

  PagedModelDebtPositionTypeOrg findDebtPositionTypeOrgNotEnabledForOperator(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, String accessToken);
}
