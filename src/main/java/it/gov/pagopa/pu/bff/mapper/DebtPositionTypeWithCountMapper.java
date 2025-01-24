package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCount;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DebtPositionTypeWithCountMapper {

  public PagedDebtPositionTypeWithCount mapToPagedDebtPositionWithCount(
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount) {
    PagedDebtPositionTypeWithCount mappedDebtPositionType = new PagedDebtPositionTypeWithCount();
    if(pagedModelDebtPositionTypeWithCount != null){
      if( pagedModelDebtPositionTypeWithCount.getEmbedded() != null
        && !CollectionUtils.isEmpty(pagedModelDebtPositionTypeWithCount.getEmbedded().getDebtPositionTypeWithCounts())){
        mappedDebtPositionType.setContent(pagedModelDebtPositionTypeWithCount.getEmbedded().getDebtPositionTypeWithCounts().stream().map(this::mapToDebtPositionTypeWithCount).toList());
      }else{
        mappedDebtPositionType.setContent(Collections.emptyList());
      }
      if(pagedModelDebtPositionTypeWithCount.getPage()!=null){
        mappedDebtPositionType.setTotalPages(pagedModelDebtPositionTypeWithCount.getPage().getTotalPages());
        mappedDebtPositionType.setSize(pagedModelDebtPositionTypeWithCount.getPage().getSize());
        mappedDebtPositionType.setNumber(pagedModelDebtPositionTypeWithCount.getPage().getNumber());
        mappedDebtPositionType.setTotalElements(pagedModelDebtPositionTypeWithCount.getPage().getTotalElements());
      }
    }
    return mappedDebtPositionType;
  }

  private DebtPositionTypeWithCount mapToDebtPositionTypeWithCount(
    it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.DebtPositionTypeWithCount debtPositionTypeWithCount) {
    return DebtPositionTypeWithCount.builder()
      .debtPositionTypeId(debtPositionTypeWithCount.getDebtPositionTypeId())
      .code(debtPositionTypeWithCount.getCode())
      .description(debtPositionTypeWithCount.getDescription())
      .updateDate(debtPositionTypeWithCount.getUpdateDate())
      .activeOrganizations(debtPositionTypeWithCount.getActiveOrganizations())
      .build();
  }
}
