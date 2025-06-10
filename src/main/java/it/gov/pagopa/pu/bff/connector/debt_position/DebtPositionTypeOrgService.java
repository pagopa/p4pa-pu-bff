package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DebtPositionTypeOrgService {
  CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, String accessToken);

  DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken);

  PagedModelDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable, String accessToken);

  CollectionModelDebtPositionTypeOrgCountByOrganizationId getDebtPositionTypeOrgCountByOrganizationId(List<Long> organizationIds, String accessToken);

  void deleteDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken);

  PagedModelDebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeId(Long debtPositionTypeId, Pageable pageable, String accessToken);

  DebtPositionTypeOrg saveDebtPositionTypeOrg(SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrg, String accessToken);

  DebtPositionTypeOrg findDebtPositionTypeOrg(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken);
}
