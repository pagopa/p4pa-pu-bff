package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationDTOMapperTest {

  private final OrganizationDTOMapper mapper = new OrganizationDTOMapper();

  @Test
  void testMapToOrganizationDTO_ValidRoles() {
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");
    organization.setOrgLogo("base64LogoString");
    organization.setOrgFiscalCode("orgFiscalCode");
    List<String> roles = Collections.singletonList("ROLE_ADMIN");
    organization.setFlagNotifyIo(false);
    organization.setFlagNotifyOutcomePush(false);
    organization.setFlagPaymentNotification(false);

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, roles);

    TestUtils.checkNotNullFields(result);

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertEquals(OrganizationDTO.OperatorRoleEnum.ROLE_ADMIN, result.getOperatorRole());
    assertEquals("base64LogoString", result.getOrgLogo());
    assertEquals("orgFiscalCode", result.getOrgFiscalCode());
    assertEquals(false, result.getFlagNotifyIo());
    assertEquals(false, result.getFlagNotifyOutcomePush());
    assertEquals(false, result.getFlagPaymentNotification());
  }

  @Test
  void testMapToOrganizationDTO_InvalidRole() {
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");
    List<String> roles = Collections.singletonList("INVALID_ROLE");

    Exception exception = assertThrows(InvalidOperatorRoleException.class, () ->
      mapper.mapToOrganizationDTO(organization, roles));

    assertEquals("INVALID_OPERATOR_ROLE: INVALID_ROLE", exception.getMessage());
  }

  @Test
  void testMapToOrganizationDTO_EmptyRoles() {
    Organization organization = new Organization();
    organization.setOrganizationId(123L);
    organization.setIpaCode("testIpaCode");
    organization.setOrgName("Test Organization");
    organization.setOrgFiscalCode("orgFiscalCode");
    organization.setFlagNotifyIo(false);
    organization.setFlagNotifyOutcomePush(false);
    organization.setFlagPaymentNotification(false);

    OrganizationDTO result = mapper.mapToOrganizationDTO(organization, Collections.emptyList());

    TestUtils.checkNotNullFields(result, "operatorRole", "orgLogo");

    assertEquals(123L, result.getOrganizationId());
    assertEquals("testIpaCode", result.getIpaCode());
    assertEquals("Test Organization", result.getOrgName());
    assertNull(result.getOperatorRole());
    assertNull(result.getOrgLogo());
    assertEquals("orgFiscalCode", result.getOrgFiscalCode());
    assertEquals(false, result.getFlagNotifyIo());
    assertEquals(false, result.getFlagNotifyOutcomePush());
    assertEquals(false, result.getFlagPaymentNotification());
  }

}
