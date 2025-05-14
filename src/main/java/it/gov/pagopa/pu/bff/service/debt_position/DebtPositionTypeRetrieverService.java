package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DebtPositionTypeRetrieverService {

  DebtPositionType getDebtPositionTypeById(String accessToken, Long id);

  PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long organizationId, String description, Pageable pageable, UserInfo loggedUser, String accessToken);

  DebtPositionTypeDetailDTO getDebtPositionTypeDetail(Long organizationId, Long debtPositionTypeId, UserInfo loggedUser, String accessToken);

  DebtPositionType createDebtPositionType(DebtPositionTypeRequestBody debtPositionType, UserInfo userInfo, String accessToken);

  DebtPositionType patchDebtPositionType(Long debtPositionTypeId, DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody, UserInfo loggedUser, String accessToken);

  void deleteDebtPositionType(Long debtPositionTypeId, UserInfo loggedUser, String accessToken);

  List<DebtPositionType> getDebtPositionTypesByOrganizationId(Long organizationId, UserInfo loggedUser, String accessToken);
}
