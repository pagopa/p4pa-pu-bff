package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.TreasuryViewSearchClient;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TreasuryServiceImpl implements TreasuryService {

  private final TreasuryViewSearchClient treasuryViewSearchClient;

  public TreasuryServiceImpl(TreasuryViewSearchClient treasuryViewSearchClient) {
    this.treasuryViewSearchClient = treasuryViewSearchClient;
  }

  public PagedModelTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, String accessToken) {
    return treasuryViewSearchClient.getTreasuries(treasuryViewFiltersDTO, pageable, accessToken);
  }

}
