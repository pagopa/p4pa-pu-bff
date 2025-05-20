package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedTaxonomy;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelTaxonomy;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TaxonomyMapper {

  public PagedTaxonomy mapToPagedTaxonomy(PagedModelTaxonomy pagedModelTaxonomy) {
    PagedTaxonomy mappedPagedTaxonomy = new PagedTaxonomy();

    if (pagedModelTaxonomy != null) {
      if (pagedModelTaxonomy.getEmbedded() != null &&
        !CollectionUtils.isEmpty(pagedModelTaxonomy.getEmbedded().getTaxonomies())) {
        mappedPagedTaxonomy.setContent(pagedModelTaxonomy.getEmbedded().getTaxonomies());
      } else {
        mappedPagedTaxonomy.setContent(Collections.emptyList());
      }

      if (pagedModelTaxonomy.getPage() != null) {
        mappedPagedTaxonomy.setTotalPages(pagedModelTaxonomy.getPage().getTotalPages());
        mappedPagedTaxonomy.setSize(pagedModelTaxonomy.getPage().getSize());
        mappedPagedTaxonomy.setNumber(pagedModelTaxonomy.getPage().getNumber());
        mappedPagedTaxonomy.setTotalElements(pagedModelTaxonomy.getPage().getTotalElements());
      }
    }

    return mappedPagedTaxonomy;
  }
}
