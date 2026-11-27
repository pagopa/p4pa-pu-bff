package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PagedOrgSubUnitOperatorsMapper {

  @Mapping(target = "mappedExternalUserId", source = "sourceOperator.operatorExternalUserId")
  @Mapping(target = "firstName", source = "operator.firstName")
  @Mapping(target = "lastName", source = "operator.lastName")
  @Mapping(target = "fiscalCode", source = "operator.fiscalCode")
  OrgSubUnitOperator toOrgSubUnitOperator(OrgSubUnitOperators sourceOperator, OperatorDTO operator);

  @Mapping(target = "mappedExternalUserId", source = "mappedExternalUserId")
  OrgSubUnitOperator toOrgSubUnitOperator(OperatorDTO operator);

  default PagedOrgSubUnitOperators map(List<OrgSubUnitOperator> content,
                                       PagedModelOrgSubUnitOperators source) {

    PagedOrgSubUnitOperators pagedOrgSubUnitOperators = new PagedOrgSubUnitOperators();

    if (source != null) {
      pagedOrgSubUnitOperators.setContent(!CollectionUtils.isEmpty(content) ? content : Collections.emptyList());

      if (source.getPage() != null) {
        pagedOrgSubUnitOperators.setSize(source.getPage().getSize());
        pagedOrgSubUnitOperators.setTotalElements(source.getPage().getTotalElements());
        pagedOrgSubUnitOperators.setTotalPages(source.getPage().getTotalPages());
        pagedOrgSubUnitOperators.setNumber(source.getPage().getNumber());
      }
    } else {
      pagedOrgSubUnitOperators.setContent(Collections.emptyList());
    }

    return pagedOrgSubUnitOperators;
  }

  default PagedOrgSubUnitOperators map(Page<OrgSubUnitOperator> page) {
    PagedOrgSubUnitOperators pagedResult = new PagedOrgSubUnitOperators();

    if (page != null) {
      pagedResult.setContent(page.getContent());
      pagedResult.setNumber((long) page.getNumber());
      pagedResult.setSize((long) page.getSize());
      pagedResult.setTotalElements(page.getTotalElements());
      pagedResult.setTotalPages((long) page.getTotalPages());
    } else {
      pagedResult.setContent(Collections.emptyList());
      pagedResult.setNumber(0L);
      pagedResult.setSize(0L);
      pagedResult.setTotalElements(0L);
      pagedResult.setTotalPages(0L);
    }

    return pagedResult;
  }
}
