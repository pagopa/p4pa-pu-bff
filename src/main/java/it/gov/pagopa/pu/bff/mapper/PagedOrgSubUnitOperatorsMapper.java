package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PagedOrgSubUnitOperatorsMapper {

  @Mapping(target = "mappedExternalUserId", expression = "java(sourceOperator != null ? sourceOperator.getOperatorExternalUserId() : null)")
  @Mapping(target = "firstName", expression = "java(userInfo != null ? userInfo.getName() : null)")
  @Mapping(target = "lastName", expression = "java(userInfo != null ? userInfo.getFamilyName() : null)")
  @Mapping(target = "fiscalCode", expression = "java(userInfo != null ? userInfo.getFiscalCode() : null)")
  OrgSubUnitOperator toOrgSubUnitOperator(OrgSubUnitOperators sourceOperator, UserInfo userInfo);

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
}
