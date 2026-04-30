package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationDetailMapper {

  @Mapping(target = "debtPositionTypeOrgCount", constant = "0")
  @Mapping(target = "operatorsCount", constant = "0")
  OrganizationDetail mapToBffDTO(it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO source);
}
