package it.gov.pagopa.pu.bff.service.debtposition;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import java.util.List;

public interface DebtPositionTypeService {

  PagedDebtPositionTypeWithCount getDebtPositionTypeWithCount(
    Long organizationId, Integer page, Long size, List<String> sort,
    UserInfo loggedUser, String accessToken);
}
