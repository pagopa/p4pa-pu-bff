package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormEntityClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.SpontaneousFormSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormServiceImpl implements SpontaneousFormService {

  private final SpontaneousFormSearchClient spontaneousFormSearchClient;
  private final SpontaneousFormEntityClient spontaneousFormEntityClient;
  private final SpontaneousFormClient spontaneousFormClient;

  public SpontaneousFormServiceImpl(SpontaneousFormSearchClient spontaneousFormSearchClient, SpontaneousFormEntityClient spontaneousFormEntityClient,
      SpontaneousFormClient spontaneousFormClient) {
    this.spontaneousFormSearchClient = spontaneousFormSearchClient;
    this.spontaneousFormEntityClient = spontaneousFormEntityClient;
    this.spontaneousFormClient = spontaneousFormClient;
  }

  @Override
  public List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken) {
    return spontaneousFormSearchClient.findAllByOrganizationId(organizationId, accessToken);
  }

  @Override
  public SpontaneousForm getSpontaneousForm(Long spontaneousFormId, String accessToken) {
    return spontaneousFormEntityClient.getSpontaneousForm(spontaneousFormId, accessToken);
  }

  @Override
  public PagedModelSpontaneousForm findAllByOrganizationIdAndCode(Long organizationId, String code, Pageable pageable, String accessToken) {
    return spontaneousFormSearchClient.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken);
  }

  @Override
  public SpontaneousForm createSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken) {
    return spontaneousFormClient.createSpontaneousForm(spontaneousForm,accessToken);
  }
}
