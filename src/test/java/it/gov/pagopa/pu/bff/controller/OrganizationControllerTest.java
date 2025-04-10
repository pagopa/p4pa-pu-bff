package it.gov.pagopa.pu.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

  @Mock
  private OrganizationRetrieverService organizationRetrieverService;

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
    when(organizationRetrieverService.getOrganizations(any(), any())).thenReturn(organizationDTOList);

    ResponseEntity<List<OrganizationDTO>> response = organizationController.getOrganizations();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("Test Organization", response.getBody().get(0).getOrgName());
    assertEquals(OrganizationDTO.OperatorRoleEnum.ADMIN, response.getBody().get(0).getOperatorRole());

    verify(organizationRetrieverService, times(1)).getOrganizations(any(), any());
  }

  @Test
  void testGetOrganizationsWithDebtPositionTypeOrgCount() {
    PagedOrganizationWithDebtPositionTypeOrgCount expected = PagedOrganizationWithDebtPositionTypeOrgCount.builder()
      .content(List.of(
        OrganizationWithDebtPositionTypeOrgCount.builder().organizationId(1L)
          .organizationName("orgName").ipaCode("ipaCode")
          .debtPositionTypeOrgCount(3).build()))
      .size(1L)
      .totalElements(1L)
      .totalPages(1L)
      .number(0L)
      .build();
    when(organizationRetrieverService.getOrganizationsWithDebtPositionTypeOrgCount(eq(1L), eq("orgName"), any(), eq(null), any()))
      .thenReturn(expected);

    ResponseEntity<PagedOrganizationWithDebtPositionTypeOrgCount> response = organizationController.getOrganizationsWithDebtPositionTypeOrgCount(1L, "orgName", Pageable.unpaged());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().getContent().size());
    assertEquals("orgName", response.getBody().getContent().getFirst().getOrganizationName());
    assertEquals(3, response.getBody().getContent().getFirst().getDebtPositionTypeOrgCount());

    verify(organizationRetrieverService, times(1)).getOrganizationsWithDebtPositionTypeOrgCount(any(), any(), any(), eq(null), any());
  }

}
