package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgServiceImpl;
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
class DebtPositionTypeOrgServiceImplTest {

  @Mock
  private DebtPositionTypeOrgClient debtPositionTypeOrgClientMock;

  private DebtPositionTypeOrgServiceImpl debtPositionTypeOrgService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgService = new DebtPositionTypeOrgServiceImpl(debtPositionTypeOrgClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgClientMock);
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeOrgsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String operatorExternalUserId = "operator-123";
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    PagedModelDebtPositionTypeOrgEmbedded embedded = new PagedModelDebtPositionTypeOrgEmbedded();
    embedded.setDebtPositionTypeOrgs(List.of(debtPositionTypeOrg));
    CollectionModelDebtPositionTypeOrg collectionModel = new CollectionModelDebtPositionTypeOrg();
    collectionModel.setEmbedded(embedded);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      Mockito.when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken))
        .thenReturn(collectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, loggedUser, accessToken);

      assertNotNull(result);
      assertFalse(result.isEmpty());
      assertSame(debtPositionTypeOrg, result.get(0));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verify(debtPositionTypeOrgClientMock).getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);
    }
  }

  @Test
  void givenEmptyResultWhenGetDebtPositionTypeOrgsThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String operatorExternalUserId = "operator-123";
    CollectionModelDebtPositionTypeOrg emptyCollectionModel = new CollectionModelDebtPositionTypeOrg();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      Mockito.when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken))
        .thenReturn(emptyCollectionModel);

      List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(result.isEmpty());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verify(debtPositionTypeOrgClientMock).getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeOrgsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String operatorExternalUserId = "operator-123";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
    }
  }
}
