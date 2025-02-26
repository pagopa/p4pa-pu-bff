package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.InstallmentClient;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstallmentServiceImpl implements InstallmentService {

  private final InstallmentClient client;

  public InstallmentServiceImpl(InstallmentClient client) {
    this.client = client;
  }

  @Override
  public PagedModelInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, String accessToken) {
    return client.getInstallments(installmentViewFiltersDTO, pageable, accessToken);
  }

}
