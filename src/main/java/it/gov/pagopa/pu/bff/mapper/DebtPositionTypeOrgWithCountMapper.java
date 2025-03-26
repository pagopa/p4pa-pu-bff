package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class DebtPositionTypeOrgWithCountMapper {

  public PagedDebtPositionTypeOrgWithCount mapToPagedDebtPositionTypeOrgWithCount(PagedModelDebtPositionTypeOrgWithCount pagedModel) {
    PagedDebtPositionTypeOrgWithCount mappedDebtPositionTypeOrgs = new PagedDebtPositionTypeOrgWithCount();

    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(pagedModel.getEmbedded().getDebtPositionTypeOrgWithCounts())) {
        mappedDebtPositionTypeOrgs.setContent(pagedModel.getEmbedded().getDebtPositionTypeOrgWithCounts());
      } else {
        mappedDebtPositionTypeOrgs.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        mappedDebtPositionTypeOrgs.setTotalPages(pagedModel.getPage().getTotalPages());
        mappedDebtPositionTypeOrgs.setSize(pagedModel.getPage().getSize());
        mappedDebtPositionTypeOrgs.setNumber(pagedModel.getPage().getNumber());
        mappedDebtPositionTypeOrgs.setTotalElements(pagedModel.getPage().getTotalElements());
      }
    }
    return mappedDebtPositionTypeOrgs;
  }

}
