package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitService;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.exception.InvalidOrgSubUnitException;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrgSubUnitService orgSubUnitServiceMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private PagedOrgSubUnitMapper pagedOrgSubUnitMapperMock;

  private OrgSubUnitRetrieverService orgSubUnitRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    orgSubUnitRetrieverService = new OrgSubUnitRetrieverServiceImpl(orgSubUnitServiceMock, authorizationServiceMock, pagedOrgSubUnitMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSubUnitServiceMock, authorizationServiceMock, pagedOrgSubUnitMapperMock);
  }


  @Test
  void givenValidIdWhenGetOrgSubUnitByIdThenReturnSubUnit() {
    // Given
    Long organizationId = 1L;
    String subUnitCode = "CODE";
    String orgSubUnitId = organizationId+"-"+subUnitCode;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
        .thenReturn(expectedResult);

      // When
      OrgSubUnit result = orgSubUnitRetrieverService.getOrgSubUnitById(organizationId, subUnitCode, loggedUser, accessToken);

      // Then
      assertNotNull(result);
      assertSame(expectedResult, result);
    }
  }

  @Test
  void givenNonExistentIdWhenGetOrgSubUnitByIdThenThrowResourceNotFoundException() {
    // Given
    Long organizationId = 1L;
    String subUnitCode = "CODE";
    String orgSubUnitId = organizationId+"-"+subUnitCode;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
        .thenReturn(null);

      // When & Then
      NotFoundException exception = assertThrows(
        NotFoundException.class,
        () -> orgSubUnitRetrieverService.getOrgSubUnitById(organizationId, subUnitCode, loggedUser, accessToken)
      );

      assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
      assertEquals("Organization SubUnit having subUnitCode " + subUnitCode + " not found", exception.getMessage());
    }
  }

  @Test
  void givenValidUserWhenCreateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    requestBody.setOrganizationId(organizationId);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.createOrgSubUnit(requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitRetrieverService.createOrgSubUnit(organizationId, requestBody, loggedUser, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

  @Test
  void givenInvalidOrgIdWhenCreateOrgSubUnitThenError() {
    // Given
    Long organizationId = 1L;
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    requestBody.setOrganizationId(2L);
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    // When & Then
    InvalidOrgSubUnitException exception = assertThrows(
      InvalidOrgSubUnitException.class,
      () -> orgSubUnitRetrieverService.createOrgSubUnit(organizationId, requestBody, loggedUser, accessToken)
    );

    assertEquals("INVALID_ORG_SUB_UNIT", exception.getCode());
    assertTrue(exception.getMessage().contains("Mismatch organizationId"));
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

  @Test
  void givenValidUserWhenDeleteOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    String subUnitCode = "CODE";
    String orgSubUnitId = organizationId+"-"+subUnitCode;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    // When & Then
    assertDoesNotThrow(() -> orgSubUnitRetrieverService.deleteOrgSubUnit(organizationId, subUnitCode, loggedUser, accessToken));

    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    verify(orgSubUnitServiceMock).deleteOrgSubUnit(orgSubUnitId, accessToken);
  }


  @Test
  void givenValidUserWhenUpdateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    String subUnitCode = "CODE";
    String orgSubUnitId = organizationId+"-"+subUnitCode;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    requestBody.setOrganizationId(organizationId);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitRetrieverService.updateOrgSubUnit(organizationId, subUnitCode, requestBody, loggedUser, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

  @Test
  void givenExistingSubUnitWhenUpdateOrgSubUnitStatusThenOk() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus orgSubUnitStatus = OrgSubUnitStatus.CANCELLED;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    String orgSubUnitId = organizationId+"-"+subUnitCode;
    OrgSubUnit existingSubUnit = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
      .thenReturn(existingSubUnit);

    assertDoesNotThrow(() -> orgSubUnitRetrieverService.updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus, loggedUser, accessToken));

    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    verify(orgSubUnitServiceMock).updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus, accessToken);
  }

  @Test
  void givenNotExistingSubUnitWhenUpdateOrgSubUnitStatusThenThrowNotFoundException() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus orgSubUnitStatus = OrgSubUnitStatus.CANCELLED;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    String orgSubUnitId = organizationId+"-"+subUnitCode;

    when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
      .thenReturn(null);

    NotFoundException exception = assertThrows(
      NotFoundException.class,
      () -> orgSubUnitRetrieverService.updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus, loggedUser, accessToken)
    );

    assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
    assertEquals("Organization SubUnit having subUnitCode " + subUnitCode + " not found", exception.getMessage());
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

  @Test
  void givenAdminUserWhenGetPagedOrgSubUnitsThenOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    PagedOrgSubUnitFiltersDTO filters = podamFactory.manufacturePojo(PagedOrgSubUnitFiltersDTO.class);
    filters.setOrganizationId(organizationId);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelOrgSubUnit pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnit.class);
    PagedOrgSubUnit expectedMappedResult = podamFactory.manufacturePojo(PagedOrgSubUnit.class);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);
      authMock.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser))
        .thenReturn(true);

      when(orgSubUnitServiceMock.findByOrganizationIdAndFilters(
        organizationId,
        filters.getMappedExternalUserId(),
        filters.getSubUnitCode(),
        filters.getStatus(),
        filters.getSubUnitType(),
        pageable,
        accessToken
      )).thenReturn(pagedModel);

      when(pagedOrgSubUnitMapperMock.map(pagedModel)).thenReturn(expectedMappedResult);

      PagedOrgSubUnit result = orgSubUnitRetrieverService.getPagedOrgSubUnits(filters, pageable, loggedUser, accessToken);

      assertEquals(expectedMappedResult, result);
    }
  }

  @Test
  void givenNonAdminUserAndMatchingAuthorizedOperatorWhenGetPagedOrgSubUnitsThenReturnOk() {
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    PagedOrgSubUnitFiltersDTO filters = podamFactory.manufacturePojo(PagedOrgSubUnitFiltersDTO.class);
    filters.setOrganizationId(organizationId);
    filters.setMappedExternalUserId(loggedUser.getMappedExternalUserId());
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelOrgSubUnit pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnit.class);
    PagedOrgSubUnit expectedMappedResult = podamFactory.manufacturePojo(PagedOrgSubUnit.class);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);
      authMock.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser))
        .thenReturn(false);

      when(orgSubUnitServiceMock.findByOrganizationIdAndFilters(
        organizationId,
        loggedUser.getMappedExternalUserId(),
        filters.getSubUnitCode(),
        filters.getStatus(),
        filters.getSubUnitType(),
        pageable,
        accessToken
      )).thenReturn(pagedModel);

      when(pagedOrgSubUnitMapperMock.map(pagedModel)).thenReturn(expectedMappedResult);

      PagedOrgSubUnit result = orgSubUnitRetrieverService.getPagedOrgSubUnits(filters, pageable, loggedUser, accessToken);

      assertEquals(expectedMappedResult, result);
    }
  }

  @Test
  void givenNonAdminUserAndMismatchingAuthorizedOperatorWhenGetPagedOrgSubUnitsThenThrowAuthorizationDeniedException() {
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    PagedOrgSubUnitFiltersDTO filters = podamFactory.manufacturePojo(PagedOrgSubUnitFiltersDTO.class);
    filters.setOrganizationId(organizationId);
    filters.setMappedExternalUserId("anotherOperator");
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);
      authMock.when(() -> AuthorizationService.isAdminRole(organizationId, loggedUser))
        .thenReturn(false);

      AuthorizationDeniedException exception = assertThrows(
        AuthorizationDeniedException.class,
        () -> orgSubUnitRetrieverService.getPagedOrgSubUnits(filters, pageable, loggedUser, accessToken)
      );
    }
  }
}
