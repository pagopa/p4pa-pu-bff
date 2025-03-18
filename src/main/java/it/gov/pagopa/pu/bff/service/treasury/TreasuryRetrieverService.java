package it.gov.pagopa.pu.bff.service.treasury;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.springframework.data.domain.Pageable;

public interface TreasuryRetrieverService {
  PagedTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  Treasury getTreasuryDetail(Long organizationId, String treasuryId, UserInfo loggedUser, String accessToken);
}
