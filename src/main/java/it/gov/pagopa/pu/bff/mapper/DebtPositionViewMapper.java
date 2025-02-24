package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DebtPositionViewMapper {
  public PagedDebtPositionView mapToPagedDebtPositionView(
    PagedModelDebtPositionView pagedModelDebtPositionView) {
    PagedDebtPositionView mappedPagedDebtPositionView = new PagedDebtPositionView();
    if(pagedModelDebtPositionView != null){
      if( pagedModelDebtPositionView.getEmbedded() != null
        && !CollectionUtils.isEmpty(pagedModelDebtPositionView.getEmbedded().getDebtPositionViews())){
        mappedPagedDebtPositionView.setContent(pagedModelDebtPositionView.getEmbedded().getDebtPositionViews());
      }else{
        mappedPagedDebtPositionView.setContent(Collections.emptyList());
      }
      if(pagedModelDebtPositionView.getPage()!=null){
        mappedPagedDebtPositionView.setTotalPages(pagedModelDebtPositionView.getPage().getTotalPages());
        mappedPagedDebtPositionView.setSize(pagedModelDebtPositionView.getPage().getSize());
        mappedPagedDebtPositionView.setNumber(pagedModelDebtPositionView.getPage().getNumber());
        mappedPagedDebtPositionView.setTotalElements(pagedModelDebtPositionView.getPage().getTotalElements());
      }
    }
    return mappedPagedDebtPositionView;
  }
}
