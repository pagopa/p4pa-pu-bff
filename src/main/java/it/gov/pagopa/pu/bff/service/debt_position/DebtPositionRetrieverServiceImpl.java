package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.DebtPositionOriginEnum;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionRetrieverServiceImpl implements DebtPositionRetrieverService {

  private final DebtPositionService debtPositionService;
  private final DebtPositionViewMapper debtPositionViewMapper;
  private static final List<String> debtPositionOriginFilterList = List.of(
    DebtPositionOriginEnum.ORDINARY.toString(),
    DebtPositionOriginEnum.ORDINARY_SIL.toString(),
    DebtPositionOriginEnum.SPONTANEOUS.toString()
  );

  public DebtPositionRetrieverServiceImpl(DebtPositionService debtPositionService,
    DebtPositionViewMapper debtPositionViewMapper) {
    this.debtPositionService = debtPositionService;
    this.debtPositionViewMapper = debtPositionViewMapper;
  }

  public PagedDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser);
    return debtPositionViewMapper.mapToPagedDebtPositionView(
      debtPositionService.getDebtPositionViews(
        filtersDTO,
        debtPositionOriginFilterList,
        loggedUser.getMappedExternalUserId(),
        pageable,
        accessToken)
    );
  }

}
