package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import org.springframework.data.domain.Pageable;

public interface DebtPositionTypeRetrieverService {

  DebtPositionType getDebtPositionTypeById(String accessToken, Long id);

  PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
    Long organizationId, Pageable pageable,
    UserInfo loggedUser, String accessToken);

  DebtPositionTypeDetailDTO getDebtPositionTypeDetail(Long organizationId, Long debtPositionTypeId, UserInfo loggedUser, String accessToken);
}
