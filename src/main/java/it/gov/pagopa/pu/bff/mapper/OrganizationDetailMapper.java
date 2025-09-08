package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationDetailMapper {

  OrganizationDetailsDTO mapToBffDTO(it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO source);
}
