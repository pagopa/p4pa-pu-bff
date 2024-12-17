package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OrganizationDTOMapperTest {

  private final OrganizationDTOMapper mapper = new OrganizationDTOMapper();

  @Test
  void testMapToOrganizationDTO() {
    EntityModelOrganization organization = new EntityModelOrganization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");

    List<String> roles = Collections.singletonList("Admin");

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, roles);

    TestUtils.checkNotNullFields(result, "orgLogo");

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertEquals("Admin", result.getOperatorRole());
    assertNull(result.getOrgLogo());
  }

  @Test
  void testMapToOrganizationDTO_EmptyRoles() {
    EntityModelOrganization organization = new EntityModelOrganization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, Collections.emptyList());

    TestUtils.checkNotNullFields(result, "operatorRole", "orgLogo");

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertNull(result.getOperatorRole());
    assertNull(result.getOrgLogo());
  }

}
