package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DebtPositionTypeService {
  DebtPositionType getDebtPositionTypeById(Long id, String accessToken);

  PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, String description, Pageable pageable, String accessToken);

  DebtPositionType createDebtPositionType(DebtPositionTypeRequestBody debtPositionType, String accessToken);

  DebtPositionType patchDebtPositionType(Long debtPositionTypeId, DebtPositionTypeRequestBody debtPositionType, String accessToken);

  void deleteDebtPositionType(Long debtPositionTypeId, String accessToken);

  CollectionModelDebtPositionType getDebtPositionTypesByBrokerIdAndOrgType(Long brokerId, String orgType, String accessToken);
  List<DebtPositionType> findByDebtPositionTypeIds(Set<Long> debtPositionTypeIds, String accessToken);
}
