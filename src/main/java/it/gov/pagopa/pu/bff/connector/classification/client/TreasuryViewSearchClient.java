package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        treasuryViewFiltersDTO.getBillDate(),
        treasuryViewFiltersDTO.getProvisionalCode(),
        treasuryViewFiltersDTO.getBillCode(),
        treasuryViewFiltersDTO.getPspLastName(),
        treasuryViewFiltersDTO.getRegionValueDate(),
        treasuryViewFiltersDTO.getDocumentCode(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
