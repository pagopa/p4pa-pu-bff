package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeRetrieverServiceImpl implements DebtPositionTypeRetrieverService {

  private final DebtPositionTypeService debtPositionTypeService;
  private final DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper;
  private final AuthorizationService authorizationService;

  public DebtPositionTypeRetrieverServiceImpl(DebtPositionTypeService debtPositionTypeService,
                                              DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper,
                                              AuthorizationService authorizationService) {
    this.debtPositionTypeService = debtPositionTypeService;
    this.debtPositionTypeWithCountMapper = debtPositionTypeWithCountMapper;
    this.authorizationService = authorizationService;
  }

  public DebtPositionType getDebtPositionTypeById(String accessToken, Long id) {
    return debtPositionTypeService.getDebtPositionTypeById(id, accessToken);
  }

  @Override
  public PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
    Long organizationId, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId,loggedUser);
    return debtPositionTypeWithCountMapper.mapToPagedDebtPositionWithCount(
      debtPositionTypeService.getDebtPositionTypeWithCount(
        loggedUser.getBrokerId(),
        pageable,
        accessToken)
    );
  }
}
