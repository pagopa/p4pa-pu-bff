package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceRequestBodyAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyBasicAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyJwtAuthConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrgSilServiceDTOMapper {
  @Mapping(target = "authConfig", ignore = true)
  @Mapping(target = "legacyBasicAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && \"legacyBasic\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "castToLegacyBasic")
  @Mapping(target = "legacyJwtAuthConfig", conditionExpression = "java(orgSilService.getAuthConfig()!=null && \"legacyJwt\".equals(orgSilService.getAuthConfig().getAuthConfig()))", source = "authConfig", qualifiedByName = "castToLegacyJwt")
  OrgSilServiceDTO map(OrgSilService orgSilService);

  List<OrgSilServiceDTO> map(List<OrgSilService> orgSilServiceList);

  @Named("castToLegacyBasic")
  static SilServiceLegacyBasicAuthConfig castToLegacyBasic(OrgSilServiceRequestBodyAuthConfig config) {
    if (config instanceof SilServiceLegacyBasicAuthConfig legacy) {
      return SilServiceLegacyBasicAuthConfig.builder()
              .authConfig(legacy.getAuthConfig())
              .authUrl(legacy.getAuthUrl())
              .user(legacy.getUser())
              .psw(legacy.getPsw())
              .build();
    }
    return null;
  }

  @Named("castToLegacyJwt")
  static SilServiceLegacyJwtAuthConfig castToLegacyJwt(OrgSilServiceRequestBodyAuthConfig config) {
    if (config instanceof SilServiceLegacyJwtAuthConfig legacy) {
      return SilServiceLegacyJwtAuthConfig.builder()
              .authConfig(legacy.getAuthConfig())
              .kid(legacy.getKid())
              .subject(legacy.getSubject())
              .issuer(legacy.getIssuer())
              .algorithm(legacy.getAlgorithm())
              .signingKey(legacy.getSigningKey())
              .build();
    }
    return null;
  }
}

