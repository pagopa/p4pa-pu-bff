package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtended;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrgSilServiceDTOMapper {
  @Mapping(target = "authConfig", ignore = true)
  @Mapping(target = "legacyBasicAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilService.getFlagLegacy()) && \"legacyBasic\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapAuthConfig")
  @Mapping(target = "legacyJwtAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilService.getFlagLegacy()) && \"legacyJwt\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapAuthConfig")
  OrgSilServiceExtended map(OrgSilService orgSilService);

  List<OrgSilServiceExtended> map(List<OrgSilService> orgSilServiceList);

  @SuppressWarnings("unchecked")
  @Named("mapAuthConfig")
  default <T extends SilServiceAuthConfig> T mapAuthConfig(OrgSilServiceRequestBodyAuthConfig config){
    return (T) config;
  }

  @Mapping(target = "authConfig", ignore = true)
  @Mapping(target = "legacyBasicAuthConfig", conditionExpression = "java(orgSilServiceDTO.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilServiceDTO.getFlagLegacy()) && \"legacyBasic\".equals(orgSilServiceDTO.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapOrgSilServiceDTOAuthConfig")
  @Mapping(target = "legacyJwtAuthConfig", conditionExpression = "java(orgSilServiceDTO.getAuthConfig()!=null && Boolean.TRUE.equals(orgSilServiceDTO.getFlagLegacy()) && \"legacyJwt\".equals(orgSilServiceDTO.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "mapOrgSilServiceDTOAuthConfig")
  OrgSilServiceDecryptedDTO map(OrgSilServiceDTO orgSilServiceDTO);

  @SuppressWarnings("unchecked")
  @Named("mapOrgSilServiceDTOAuthConfig")
  default <T extends SilServiceAuthConfigDTO> T mapOrgSilServiceDTOAuthConfig(OrgSilServiceDTOAuthConfig config){
    return (T) config;
  }
}

