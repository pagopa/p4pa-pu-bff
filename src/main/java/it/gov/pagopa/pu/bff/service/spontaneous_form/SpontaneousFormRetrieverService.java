package it.gov.pagopa.pu.bff.service.spontaneous_form;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.dto.generated.SpontaneousFormDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpontaneousFormRetrieverService {
  List<SpontaneousForm> getSpontaneousForms(Long organizationId, UserInfo loggedUser, String accessToken);
  SpontaneousForm getSpontaneousFormAndValidate(Long spontaneousFormId, DebtPositionTypeOrg debtPositionTypeOrg, String accessToken);
  PagedSpontaneousForm getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable, UserInfo loggedUser, String accessToken);
  SpontaneousFormDetailDTO getSpontaneousFormDetail(Long organizationId, Long spontaneousFormId, UserInfo loggedUser, String accessToken);
  SpontaneousForm createSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, UserInfo loggedUser, String accessToken);
  void deleteSpontaneousForm(Long organizationId, Long spontaneousFormId, UserInfo loggedUser, String accessToken);
  void updateSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm, UserInfo loggedUser, String accessToken);
}
