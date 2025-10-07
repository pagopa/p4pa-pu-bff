package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface SpontaneousFormRetrieverService {
  List<SpontaneousForm> getSpontaneousForms(Long organizationId, UserInfo loggedUser, String accessToken);
  SpontaneousForm getSpontaneousFormAndValidate(Long spontaneousFormId, DebtPositionTypeOrg debtPositionTypeOrg, String accessToken);
  PagedSpontaneousForm getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable, UserInfo loggedUser, String accessToken);
}
