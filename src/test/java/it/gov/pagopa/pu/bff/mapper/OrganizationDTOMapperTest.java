package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Organization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrganizationDTOMapperTest {

  private final OrganizationDTOMapper mapper = new OrganizationDTOMapper();

  @Test
  void testMapToOrganizationDTO() {
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");
    organization.setOrgLogoDesc("base64LogoString");
    List<String> roles = Arrays.asList("Admin", "User");

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, roles);

    TestUtils.checkNotNullFields(result);

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertEquals(roles, result.getOperatorRole());
    assertEquals("base64LogoString", result.getOrgLogo());
  }

  @Test
  void testMapToOrganizationDTO_EmptyRoles() {
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, Collections.emptyList());

    TestUtils.checkNotNullFields(result, "operatorRole", "orgLogo");

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertTrue(result.getOperatorRole().isEmpty());
    assertNull(result.getOrgLogo());
  }

}
