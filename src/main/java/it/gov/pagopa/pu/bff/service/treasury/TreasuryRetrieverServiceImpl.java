package it.gov.pagopa.pu.bff.service.treasury;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.TreasuryService;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.mapper.TreasuryViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TreasuryRetrieverServiceImpl implements TreasuryRetrieverService {

  private final TreasuryService treasuryService;
  private final TreasuryViewMapper treasuryViewMapper;

  public TreasuryRetrieverServiceImpl(TreasuryService treasuryService, TreasuryViewMapper treasuryViewMapper) {
    this.treasuryService = treasuryService;
    this.treasuryViewMapper = treasuryViewMapper;
  }

  @Override
  public PagedTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    DateUtils.validateDateFilters(treasuryViewFiltersDTO.getBillDateFilter(), "billDate");
    DateUtils.validateDateFilters(treasuryViewFiltersDTO.getRegionValueDateFilter(), "regionValueDate");

    AuthorizationService.validateUserForOrganizationId(treasuryViewFiltersDTO.getOrganizationId(), loggedUser);

    return treasuryViewMapper.mapToPagedTreasury(
      treasuryService.getTreasuries(treasuryViewFiltersDTO, pageable, accessToken));
  }

  @Override
  public Treasury getTreasuryDetail(Long organizationId, String treasuryId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    return treasuryService.getTreasuryDetail(organizationId, treasuryId, accessToken);
  }

}
