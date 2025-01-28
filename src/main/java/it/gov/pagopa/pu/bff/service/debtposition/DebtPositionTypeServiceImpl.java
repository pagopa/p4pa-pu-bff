package it.gov.pagopa.pu.bff.service.debtposition;

import it.gov.pagopa.pu.bff.connector.debtposition.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DebtPositionTypeServiceImpl implements DebtPositionTypeService {
  private final DebtPositionClient debtPositionClient;
  private final DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper;
  private final AuthorizationService authorizationService;

  public DebtPositionTypeServiceImpl(DebtPositionClient debtPositionClient,
    DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapper,
    AuthorizationService authorizationService) {
    this.debtPositionClient = debtPositionClient;
    this.debtPositionTypeWithCountMapper = debtPositionTypeWithCountMapper;
    this.authorizationService = authorizationService;
  }

  @Override
  public PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
      Long organizationId, Pageable pageable,
      UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId,loggedUser);
    return debtPositionTypeWithCountMapper.mapToPagedDebtPositionWithCount(
      debtPositionClient.getDebtPositionTypeWithCount(
        loggedUser.getBrokerId(),
        pageable,
        accessToken)
    );
  }
}
