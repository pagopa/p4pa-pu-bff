package it.gov.pagopa.pu.bff.service.treasury;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import org.springframework.data.domain.Pageable;

public interface TreasuryRetrieverService {
  PagedTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
}
