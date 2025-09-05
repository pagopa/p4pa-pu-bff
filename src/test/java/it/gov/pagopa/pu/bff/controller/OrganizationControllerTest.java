package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgCount;

import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

  @Mock
  private OrganizationRetrieverService organizationRetrieverServiceMock;

  @InjectMocks
  private OrganizationController organizationController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  private List<OrganizationDTO> organizationDTOList;

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);

    organizationDTOList = new ArrayList<>();
    OrganizationDTO.OperatorRoleEnum operatorRole = OrganizationDTO.OperatorRoleEnum.ROLE_ADMIN;

    OrganizationDTO organizationDTO = OrganizationDTO.builder()
      .organizationId(123L)
      .ipaCode("IPA001")
      .orgName("Test Organization")
      .operatorRole(operatorRole)
      .build();

    organizationDTOList.add(organizationDTO);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      organizationRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void testGetOrganizations() {
    when(organizationRetrieverServiceMock.getOrganizations(Mockito.same(loggedUser), Mockito.same(accessToken))).thenReturn(organizationDTOList);

    ResponseEntity<List<OrganizationDTO>> response = organizationController.getOrganizations();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Test Organization", response.getBody().getFirst().getOrgName());
    assertEquals(OrganizationDTO.OperatorRoleEnum.ROLE_ADMIN, response.getBody().getFirst().getOperatorRole());
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
    when(organizationRetrieverServiceMock.getOrganizationsWithDebtPositionTypeOrgCount(eq(1L), eq("orgName"), any(), Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expected);

    ResponseEntity<PagedOrganizationWithDebtPositionTypeOrgCount> response = organizationController.getOrganizationsWithDebtPositionTypeOrgCount(1L, "orgName", Pageable.unpaged());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getContent().size());
    assertEquals("orgName", response.getBody().getContent().getFirst().getOrganizationName());
    assertEquals(3, response.getBody().getContent().getFirst().getDebtPositionTypeOrgCount());
  }

  @Test
  void givenPageableWhenGetOrganizationsByBrokerIdThenReturnPagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount() {
    //given
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount = new PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount();
    Mockito.when(organizationRetrieverServiceMock.getOrganizationsByBrokerId(eq(loggedUser), any(Pageable.class), eq(accessToken))).thenReturn(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount);
    //when
    ResponseEntity<PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount> result = organizationController.getOrganizationsByBrokerId(Pageable.ofSize(1));
    //then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount, result.getBody());
  }

  @Test
  void givenOrganizationIdWhenGetOrganizationDetailThenReturnOrganizationDetailDTO() {
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();
    Long organizationId = 1L;

    Mockito.when(organizationRetrieverServiceMock.getOrganizationDetail(organizationId, loggedUser, accessToken)).thenReturn(organizationDetailDTO);

    ResponseEntity<OrganizationDetailDTO> result = organizationController.getOrganizationDetail(organizationId);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(organizationDetailDTO, result.getBody());
  }
}
