package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgEmbedded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;

  private DebtPositionTypeOrgRetrieverServiceImpl debtPositionTypeOrgService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgService = new DebtPositionTypeOrgRetrieverServiceImpl(debtPositionTypeOrgServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgServiceMock);
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
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

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
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

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

}
