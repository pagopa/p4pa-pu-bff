package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeDTOMapper;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeServiceImpl implements DebtPositionTypeService {

  private final DebtPositionTypeClient debtPositionTypeClient;
  private final DebtPositionTypeDTOMapper debtPositionTypeDTOMapper;

  public DebtPositionTypeServiceImpl(DebtPositionTypeClient debtPositionTypeClient, DebtPositionTypeDTOMapper debtPositionTypeDTOMapper) {
    this.debtPositionTypeClient = debtPositionTypeClient;
    this.debtPositionTypeDTOMapper = debtPositionTypeDTOMapper;
  }

  public DebtPositionTypeDTO getDebtPositionTypeById(String accessToken, Long id) {
    return debtPositionTypeDTOMapper.mapToDebtPositionTypeDTO(
      debtPositionTypeClient.getDebtPositionTypeById(id, accessToken));
  }

}
