package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.springframework.data.domain.Pageable;

public interface TreasuryService {

  PagedModelTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, String accessToken);
}
