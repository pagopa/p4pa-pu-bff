package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.TreasuryClient;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TreasuryServiceImpl implements TreasuryService {

  private final TreasuryClient treasuryClient;

  public TreasuryServiceImpl(TreasuryClient treasuryClient) {
    this.treasuryClient = treasuryClient;
  }

  public PagedModelTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, String accessToken) {
    return treasuryClient.getTreasuries(treasuryViewFiltersDTO, pageable, accessToken);
  }

  public Treasury getTreasuryDetail(Long organizationId, String treasuryId, String accessToken) {
    return treasuryClient.getTreasuryDetail(organizationId, treasuryId, accessToken);
  }

}
