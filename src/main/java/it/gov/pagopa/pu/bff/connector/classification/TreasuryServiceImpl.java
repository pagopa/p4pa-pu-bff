package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.TreasurySearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.TreasuryViewSearchClient;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TreasuryServiceImpl implements TreasuryService {

  private final TreasuryViewSearchClient treasuryViewSearchClient;
  private final TreasurySearchClient treasurySearchClient;

  public TreasuryServiceImpl(TreasuryViewSearchClient treasuryViewSearchClient, TreasurySearchClient treasurySearchClient) {
    this.treasuryViewSearchClient = treasuryViewSearchClient;
    this.treasurySearchClient = treasurySearchClient;
  }

  public PagedModelTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, String accessToken) {
    return treasuryViewSearchClient.getTreasuries(treasuryViewFiltersDTO, pageable, accessToken);
  }

  public Treasury getTreasuryDetail(Long organizationId, String treasuryId, String accessToken) {
    return treasurySearchClient.getTreasuryDetail(organizationId, treasuryId, accessToken);
  }

}
