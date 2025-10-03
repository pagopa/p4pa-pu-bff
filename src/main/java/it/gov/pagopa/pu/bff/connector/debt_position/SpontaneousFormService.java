package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;

public interface SpontaneousFormService {
  List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken);
  SpontaneousForm getSpontaneousForm(Long spontaneousFormId, String accessToken);
}
