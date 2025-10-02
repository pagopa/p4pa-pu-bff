package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormServiceImpl implements SpontaneousFormService {

  private final SpontaneousFormSearchClient spontaneousFormSearchClient;

  public SpontaneousFormServiceImpl(SpontaneousFormSearchClient spontaneousFormSearchClient) {
    this.spontaneousFormSearchClient = spontaneousFormSearchClient;
  }

  @Override
  public List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken) {
    return spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);
  }

}