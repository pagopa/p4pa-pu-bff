package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgWithCountMapper;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;

  @Mock
  private DebtPositionTypeOrgWithCountMapper debtPositionTypeOrgWithCountMapperMock;

  @Mock
  private AuthorizationService authorizationServiceMock;

  private DebtPositionTypeOrgRetrieverServiceImpl debtPositionTypeOrgService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgService = new DebtPositionTypeOrgRetrieverServiceImpl(debtPositionTypeOrgServiceMock, debtPositionTypeOrgWithCountMapperMock, authorizationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgServiceMock, debtPositionTypeOrgWithCountMapperMock, authorizationServiceMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgByIdThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken))
        .thenReturn(expectedResult);

      DebtPositionTypeOrg result = debtPositionTypeOrgService.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgByIdThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
    }
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    PagedModelDebtPositionTypeOrgEmbedded embedded = new PagedModelDebtPositionTypeOrgEmbedded();
    embedded.setDebtPositionTypeOrgs(List.of(debtPositionTypeOrg));
    CollectionModelDebtPositionTypeOrg collectionModel = new CollectionModelDebtPositionTypeOrg();
    collectionModel.setEmbedded(embedded);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(collectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertFalse(result.isEmpty());
      assertSame(debtPositionTypeOrg, result.get(0));
      assertEquals(embedded.getDebtPositionTypeOrgs().size(), result.size());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrgs(organizationId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }


  @Test
  void givenEmptyResultWhenGetDebtPositionTypeOrgsThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    CollectionModelDebtPositionTypeOrg emptyCollectionModel = new CollectionModelDebtPositionTypeOrg();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgs(organizationId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(emptyCollectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(result.isEmpty());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrgs(organizationId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }


  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidAdminWhenGetDebtPositionTypeOrgWithCountThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);
    PagedModelDebtPositionTypeOrgWithCount pagedModelDebtPositionTypeOrgWithCount = new PagedModelDebtPositionTypeOrgWithCount();
    PagedDebtPositionTypeOrgWithCount mappedDebtPositionTypeOrgs = new PagedDebtPositionTypeOrgWithCount();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken))
      .thenReturn(pagedModelDebtPositionTypeOrgWithCount);

    Mockito.when(debtPositionTypeOrgWithCountMapperMock.mapToPagedDebtPositionTypeOrgWithCount(pagedModelDebtPositionTypeOrgWithCount))
      .thenReturn(mappedDebtPositionTypeOrgs);

    PagedDebtPositionTypeOrgWithCount result = debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(mappedDebtPositionTypeOrgs, result);

    Mockito.verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.verify(debtPositionTypeOrgServiceMock).getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);
    Mockito.verify(debtPositionTypeOrgWithCountMapperMock).mapToPagedDebtPositionTypeOrgWithCount(pagedModelDebtPositionTypeOrgWithCount);
  }

  @Test
  void givenNonAdminWhenGetDebtPositionTypeOrgWithCountThenUnauthorized() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-456"); // Non-admin user

    long organizationId = 1L;
    String code = "code";
    String description = "description";
    Pageable pageable = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId()))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    assertThrows(AuthorizationDeniedException.class, () -> debtPositionTypeOrgService.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.verify(debtPositionTypeOrgServiceMock, Mockito.never()).getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);
    Mockito.verify(debtPositionTypeOrgWithCountMapperMock, Mockito.never()).mapToPagedDebtPositionTypeOrgWithCount(Mockito.any());
  }

}

