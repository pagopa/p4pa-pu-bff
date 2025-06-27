package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.OrgSilServiceDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyBasicAuthConfig;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyJwtAuthConfig;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrgSilServiceDTOMapperTest {

  private final OrgSilServiceDTOMapper mapper = Mappers.getMapper(OrgSilServiceDTOMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenLegacyBasicAuthWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    SilServiceLegacyBasicAuthConfig basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfig.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    SilServiceLegacyJwtAuthConfig jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfig.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyJwtAuthConfig());
    assertNull(result.getLegacyBasicAuthConfig());
  }
}
