package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormEntityClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormServiceImpl implements SpontaneousFormService {

  private final SpontaneousFormSearchClient spontaneousFormSearchClient;
  private final SpontaneousFormEntityClient spontaneousFormEntityClient;

  public SpontaneousFormServiceImpl(SpontaneousFormSearchClient spontaneousFormSearchClient, SpontaneousFormEntityClient spontaneousFormEntityClient) {
    this.spontaneousFormSearchClient = spontaneousFormSearchClient;
    this.spontaneousFormEntityClient = spontaneousFormEntityClient;
  }

  @Override
  public List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken) {
    return spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);
  }

  @Override
  public SpontaneousForm getSpontaneousForm(Long spontaneousFormId, String accessToken) {
    return spontaneousFormEntityClient.getSpontaneousForm(spontaneousFormId, accessToken);
  }
}
