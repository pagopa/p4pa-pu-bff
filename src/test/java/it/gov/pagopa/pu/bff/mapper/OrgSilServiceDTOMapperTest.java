package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;

class OrgSilServiceDTOMapperTest {

  private final OrgSilServiceDTOMapper mapper = Mappers.getMapper(OrgSilServiceDTOMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenLegacyBasicAuthWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(true);
    SilServiceLegacyBasicAuthConfig basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfig.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyBasicAuthAndFlagLegacyFalseWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(false);
    SilServiceLegacyBasicAuthConfig basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfig.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyBasicAuthAndFlagLegacyNullWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(null);
    SilServiceLegacyBasicAuthConfig basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfig.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","flagLegacy","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(true);
    SilServiceLegacyJwtAuthConfig jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfig.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyJwtAuthConfig());
    assertNull(result.getLegacyBasicAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthAndFlagLegacyFalseWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(false);
    SilServiceLegacyJwtAuthConfig jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfig.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthAndFlagLegacyNullWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setFlagLegacy(null);
    SilServiceLegacyJwtAuthConfig jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfig.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","flagLegacy","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenNullAuthConfigWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    OrgSilService orgSilService = podamFactory.manufacturePojo(
            OrgSilService.class);
    orgSilService.setAuthConfig(null);

    OrgSilServiceExtendedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyBasicAuthConfigDTOWhenMapToOrgSilServiceDecryptedDTOThenCorrectMapping() {
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);
    orgSilService.setFlagLegacy(true);
    SilServiceLegacyBasicAuthConfigDTO basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfigDTO.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceDecryptedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyBasicAuthDTOAndFlagLegacyFalseWhenMapToOrgSilServiceDecryptedDTOThenCorrectMapping() {
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);
    orgSilService.setFlagLegacy(false);
    SilServiceLegacyBasicAuthConfigDTO basicAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyBasicAuthConfigDTO.class);
    basicAuthConfig.setAuthConfig("legacyBasic");
    orgSilService.setAuthConfig(basicAuthConfig);

    OrgSilServiceDecryptedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthWhenMapToOrgSilServiceDecryptedDTOThenCorrectMapping() {
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);
    orgSilService.setFlagLegacy(true);
    SilServiceLegacyJwtAuthConfigDTO jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfigDTO.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceDecryptedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    TestUtils.reflectionEqualsByName(orgSilService.getAuthConfig(),result.getLegacyJwtAuthConfig());
    assertNull(result.getLegacyBasicAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthAndFlagLegacyFalseWhenMapToOrgSilServiceDecryptedDTOThenCorrectMapping() {
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);
    orgSilService.setFlagLegacy(false);
    SilServiceLegacyJwtAuthConfigDTO jwtAuthConfig = podamFactory.manufacturePojo(SilServiceLegacyJwtAuthConfigDTO.class);
    jwtAuthConfig.setAuthConfig("legacyJwt");
    orgSilService.setAuthConfig(jwtAuthConfig);

    OrgSilServiceDecryptedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenNullAuthConfigWhenMapToOrgSilServiceDecryptedDTOThenCorrectMapping() {
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);
    orgSilService.setAuthConfig(null);

    OrgSilServiceDecryptedDTO result = mapper.map(orgSilService);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result,"authConfig","legacyJwtAuthConfig","legacyBasicAuthConfig");
    TestUtils.reflectionEqualsByName(orgSilService,result,"authConfig");
    assertNull(result.getLegacyBasicAuthConfig());
    assertNull(result.getLegacyJwtAuthConfig());
  }

  @Test
  void givenLegacyBasicAuthWhenToOrgSilServiceDTOThenCorrectAuthConfigMapped() {
    OrgSilServiceDecryptedDTO dto = new OrgSilServiceDecryptedDTO();
    dto.setFlagLegacy(true);
    dto.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());
    dto.setLegacyJwtAuthConfig(null);
    dto.setOrganizationId(1L);
    dto.setApplicationName("appName");
    dto.setServiceUrl("url");
    dto.setServiceType(OrgSilServiceType.ACTUALIZATION);

    OrgSilServiceDTO result = mapper.toOrgSilServiceDTO(dto);

    assertNotNull(result);
    assertEquals(dto.getLegacyBasicAuthConfig(), result.getAuthConfig());
  }

  @Test
  void givenLegacyJwtAuthWhenToOrgSilServiceDTOThenCorrectAuthConfigMapped() {
    OrgSilServiceDecryptedDTO dto = new OrgSilServiceDecryptedDTO();
    dto.setFlagLegacy(true);
    dto.setLegacyJwtAuthConfig(new SilServiceLegacyJwtAuthConfigDTO());
    dto.setLegacyBasicAuthConfig(null);
    dto.setOrganizationId(1L);
    dto.setApplicationName("appName");
    dto.setServiceUrl("url");
    dto.setServiceType(OrgSilServiceType.ACTUALIZATION);

    OrgSilServiceDTO result = mapper.toOrgSilServiceDTO(dto);

    assertNotNull(result);
    assertEquals(dto.getLegacyJwtAuthConfig(), result.getAuthConfig());
  }

  @Test
  void givenBothAuthConfigsWhenToOrgSilServiceDTOThenThrowBadRequest() {
    OrgSilServiceDecryptedDTO dto = new OrgSilServiceDecryptedDTO();
    dto.setFlagLegacy(true);
    dto.setLegacyJwtAuthConfig(new SilServiceLegacyJwtAuthConfigDTO());
    dto.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> mapper.toOrgSilServiceDTO(dto));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertTrue(ex.getReason().contains("only one of legacyBasicAuthConfig or legacyJwtAuthConfig"));
  }

  @Test
  void givenLegacyTrueAndNoAuthConfigWhenToOrgSilServiceDTOThenAuthConfigIsNull() {
    OrgSilServiceDecryptedDTO dto = new OrgSilServiceDecryptedDTO();
    dto.setFlagLegacy(true);
    dto.setLegacyJwtAuthConfig(null);
    dto.setLegacyBasicAuthConfig(null);
    dto.setOrganizationId(1L);
    dto.setApplicationName("appName");
    dto.setServiceUrl("url");
    dto.setServiceType(OrgSilServiceType.ACTUALIZATION);

    OrgSilServiceDTO result = mapper.toOrgSilServiceDTO(dto);

    assertNotNull(result);
    assertNull(result.getAuthConfig());
  }

  @Test
  void givenFlagLegacyFalseWhenToOrgSilServiceDTOThenAuthConfigIsNull() {
    OrgSilServiceDecryptedDTO dto = new OrgSilServiceDecryptedDTO();
    dto.setFlagLegacy(false);
    dto.setLegacyJwtAuthConfig(new SilServiceLegacyJwtAuthConfigDTO());
    dto.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());
    dto.setOrganizationId(1L);
    dto.setApplicationName("appName");
    dto.setServiceUrl("url");
    dto.setServiceType(OrgSilServiceType.ACTUALIZATION);

    OrgSilServiceDTO result = mapper.toOrgSilServiceDTO(dto);

    assertNotNull(result);
    assertNull(result.getAuthConfig());
  }
}
