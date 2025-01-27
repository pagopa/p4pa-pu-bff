package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;

public interface DebtPositionTypeService {

  DebtPositionTypeDTO getDebtPositionTypeById(String accessToken, Long id);

}
