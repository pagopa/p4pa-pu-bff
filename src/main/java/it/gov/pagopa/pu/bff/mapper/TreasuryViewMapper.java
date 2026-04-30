package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class TreasuryViewMapper {

  public PagedTreasuryView mapToPagedTreasury(PagedModelTreasuryView pagedModel) {
    PagedTreasuryView mappedTreasury = new PagedTreasuryView();

    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(pagedModel.getEmbedded().getTreasuryViews())) {
        mappedTreasury.setContent(pagedModel.getEmbedded().getTreasuryViews());
      } else {
        mappedTreasury.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        mappedTreasury.setTotalPages(pagedModel.getPage().getTotalPages());
        mappedTreasury.setSize(pagedModel.getPage().getSize());
        mappedTreasury.setNumber(pagedModel.getPage().getNumber());
        mappedTreasury.setTotalElements(pagedModel.getPage().getTotalElements());
      }
    }
    return mappedTreasury;
  }

}
