package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.service.organization.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

  @Mock
  private OrganizationService organizationService;

  @InjectMocks
  private OrganizationController organizationController;

  private List<OrganizationDTO> organizationDTOList;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    organizationDTOList = new ArrayList<>();
    OrganizationDTO.OperatorRoleEnum operatorRole = OrganizationDTO.OperatorRoleEnum.ADMIN;

    OrganizationDTO organizationDTO = OrganizationDTO.builder()
      .organizationId(123L)
      .ipaCode("IPA001")
      .orgName("Test Organization")
      .operatorRole(operatorRole)
      .build();

    organizationDTOList.add(organizationDTO);
  }

  @Test
  void testGetOrganizations() {
    when(organizationService.getOrganizations(any(), any())).thenReturn(organizationDTOList);

    ResponseEntity<List<OrganizationDTO>> response = organizationController.getOrganizations();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("Test Organization", response.getBody().get(0).getOrgName());
    assertEquals(OrganizationDTO.OperatorRoleEnum.ADMIN, response.getBody().get(0).getOperatorRole());

    verify(organizationService, times(1)).getOrganizations(any(), any());
  }

}
