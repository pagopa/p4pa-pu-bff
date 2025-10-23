package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
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
    return classificationApisHolder.getTreasuryViewSearchControllerApi(accessToken)
      .crudTreasuriesViewFindTreasuriesByFilters(
        treasuryViewFiltersDTO.getOrganizationId(),
        treasuryViewFiltersDTO.getIuv(),
        treasuryViewFiltersDTO.getIuf(),
        treasuryViewFiltersDTO.getBillAmountCents(),
        treasuryViewFiltersDTO.getBillDateFilter().getFrom(),
        treasuryViewFiltersDTO.getBillDateFilter().getTo(),
        treasuryViewFiltersDTO.getProvisionalCode(),
        treasuryViewFiltersDTO.getProvisionalAe(),
        treasuryViewFiltersDTO.getBillCode(),
        treasuryViewFiltersDTO.getBillYear(),
        treasuryViewFiltersDTO.getPspLastName(),
        treasuryViewFiltersDTO.getRegionValueDateFilter().getFrom(),
        treasuryViewFiltersDTO.getRegionValueDateFilter().getTo(),
        treasuryViewFiltersDTO.getDocumentCode(),
        treasuryViewFiltersDTO.getDocumentYear(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
