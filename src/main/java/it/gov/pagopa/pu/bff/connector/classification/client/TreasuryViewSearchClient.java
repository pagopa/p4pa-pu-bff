package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TreasuryViewSearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public TreasuryViewSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelTreasuryView getTreasuries(TreasuryViewFiltersDTO treasuryViewFiltersDTO, Pageable pageable, String accessToken) {
    LocalDateIntervalFilter billDateFilter = treasuryViewFiltersDTO.getBillDateFilter();
    LocalDateIntervalFilter regionalValueDateFilter = treasuryViewFiltersDTO.getRegionValueDateFilter();

    return classificationApisHolder.getTreasuryViewSearchControllerApi(accessToken)
      .crudTreasuriesViewFindTreasuriesByFilters(
        treasuryViewFiltersDTO.getOrganizationId(),
        treasuryViewFiltersDTO.getIuv(),
        treasuryViewFiltersDTO.getIuf(),
        treasuryViewFiltersDTO.getBillAmountCents(),
        billDateFilter != null ? billDateFilter.getFrom() : null,
        billDateFilter != null ? billDateFilter.getTo() : null,
        treasuryViewFiltersDTO.getProvisionalCode(),
        treasuryViewFiltersDTO.getProvisionalAe(),
        treasuryViewFiltersDTO.getBillCode(),
        treasuryViewFiltersDTO.getBillYear(),
        treasuryViewFiltersDTO.getPspLastName(),
        regionalValueDateFilter != null ? regionalValueDateFilter.getFrom() : null,
        regionalValueDateFilter != null ? regionalValueDateFilter.getTo() : null,
        treasuryViewFiltersDTO.getDocumentCode(),
        treasuryViewFiltersDTO.getDocumentYear(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
