package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class TreasuryClient {

  private final ClassificationApisHolder classificationApisHolder;

  public TreasuryClient(ClassificationApisHolder classificationApisHolder) {
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

  public Treasury getTreasuryDetail(Long organizationId, String treasuryId, String accessToken) {
    try {
      return classificationApisHolder.getTreasurySearchControllerApi(accessToken)
        .crudTreasuryFindByOrganizationIdAndTreasuryId(organizationId, treasuryId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("TreasuryDetail with organizationId {} and treasuryId {} not found", organizationId, treasuryId);
      return null;
    }
  }

}
