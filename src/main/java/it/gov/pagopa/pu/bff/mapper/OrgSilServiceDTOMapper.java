package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceRequestBodyAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceAuthConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrgSilServiceDTOMapper {
  @Mapping(target = "authConfig", ignore = true)
  @Mapping(target = "legacyBasicAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilService.getFlagLegacy()) && \"legacyBasic\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapAuthConfig")
  @Mapping(target = "legacyJwtAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilService.getFlagLegacy()) && \"legacyJwt\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapAuthConfig")
  OrgSilServiceDTO map(OrgSilService orgSilService);

  List<OrgSilServiceDTO> map(List<OrgSilService> orgSilServiceList);

  @SuppressWarnings("unchecked")
  @Named("mapAuthConfig")
  default <T extends SilServiceAuthConfig> T mapAuthConfig(OrgSilServiceRequestBodyAuthConfig config){
    return (T) config;
  }
}

