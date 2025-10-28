package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpontaneousFormService {
  List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken);
  SpontaneousForm getSpontaneousForm(Long spontaneousFormId, String accessToken);
  PagedModelSpontaneousForm findAllByOrganizationIdAndCode(Long organizationId, String code, Pageable pageable, String accessToken);
  SpontaneousForm createSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken);
  void deleteSpontaneousForm(Long spontaneousFormId, String accessToken);
  void updateSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken);
}
