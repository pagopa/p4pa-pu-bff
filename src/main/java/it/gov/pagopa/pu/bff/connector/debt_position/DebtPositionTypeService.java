package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.springframework.data.domain.Pageable;

public interface DebtPositionTypeService {
  DebtPositionType getDebtPositionTypeById(Long id, String accessToken);
  PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, Pageable pageable, String accessToken);
}
