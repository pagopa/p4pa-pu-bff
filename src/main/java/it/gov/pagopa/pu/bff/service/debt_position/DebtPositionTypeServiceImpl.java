package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeServiceImpl implements DebtPositionTypeService {

  private final DebtPositionTypeClient debtPositionTypeClient;
  private final DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper;
  private final AuthorizationService authorizationService;

  public DebtPositionTypeServiceImpl(DebtPositionTypeClient debtPositionTypeClient,
    DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper,
    AuthorizationService authorizationService) {
    this.debtPositionTypeClient = debtPositionTypeClient;
    this.debtPositionTypeWithCountMapper = debtPositionTypeWithCountMapper;
    this.authorizationService = authorizationService;
  }

  public DebtPositionType getDebtPositionTypeById(String accessToken, Long id) {
    return debtPositionTypeClient.getDebtPositionTypeById(id, accessToken);
  }

  @Override
  public PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
    Long organizationId, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId,loggedUser);
    return debtPositionTypeWithCountMapper.mapToPagedDebtPositionWithCount(
      debtPositionTypeClient.getDebtPositionTypeWithCount(
        loggedUser.getBrokerId(),
        pageable,
        accessToken)
    );
  }
}
