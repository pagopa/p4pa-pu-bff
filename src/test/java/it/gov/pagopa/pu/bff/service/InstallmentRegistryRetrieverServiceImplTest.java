package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.InstallmentRegistryService;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.installment_registry.InstallmentRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.installment_registry.InstallmentRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
import it.gov.pagopa.pu.registries.dto.generated.InstallmentRegistry;
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
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentRegistryRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private InstallmentRegistryService installmentRegistryServiceMock;
  @Mock
  private DebtPositionRetrieverService debtPositionRetrieverServiceMock;

  private InstallmentRegistryRetrieverService installmentRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    installmentRegistryRetrieverService = new InstallmentRegistryRetrieverServiceImpl(
      installmentRegistryServiceMock,
      debtPositionRetrieverServiceMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      installmentRegistryServiceMock,
      debtPositionRetrieverServiceMock
    );
  }

  @Test
  void givenValidUserAndValidDebtPositionIdWhenGetInstallmentRegistriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String nav = "nav";
    CollectionModelInstallmentRegistry collectionModelInstallmentRegistry = podamFactory.manufacturePojo(CollectionModelInstallmentRegistry.class);
    List<InstallmentRegistry> expectedResult = collectionModelInstallmentRegistry.getEmbedded().getInstallmentRegistries();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
      when(installmentRegistryServiceMock.getInstallmentRegistries(debtPositionId, nav, accessToken))
        .thenReturn(collectionModelInstallmentRegistry);

      List<InstallmentRegistry> result = installmentRegistryRetrieverService.getInstallmentRegistries(organizationId, debtPositionId, nav, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNullEmbeddedInstallmentRegistryCollectionWhenGetInstallmentRegistriesThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String nav = "nav";
    CollectionModelInstallmentRegistry collectionModelInstallmentRegistry = podamFactory.manufacturePojo(CollectionModelInstallmentRegistry.class);
    collectionModelInstallmentRegistry.setEmbedded(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
      when(installmentRegistryServiceMock.getInstallmentRegistries(debtPositionId, nav, accessToken))
        .thenReturn(collectionModelInstallmentRegistry);

      List<InstallmentRegistry> result = installmentRegistryRetrieverService.getInstallmentRegistries(organizationId, debtPositionId, nav, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(CollectionUtils.isEmpty(result));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNullInstallmentRegistryCollectionWhenGetInstallmentRegistriesThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String nav = "nav";
    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
      when(installmentRegistryServiceMock.getInstallmentRegistries(debtPositionId, nav, accessToken))
        .thenReturn(null);

      List<InstallmentRegistry> result = installmentRegistryRetrieverService.getInstallmentRegistries(organizationId, debtPositionId, nav, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(CollectionUtils.isEmpty(result));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentRegistriesThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String nav = "nav";
    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      doThrow(new NotFoundException("DEBT_POSITION_NOT_FOUND", "DebtPosition not found"))
        .when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

      Assertions.assertThrows(NotFoundException.class, () ->
        installmentRegistryRetrieverService.getInstallmentRegistries(organizationId, debtPositionId, nav, loggedUser, accessToken));

      Mockito.verifyNoInteractions(installmentRegistryServiceMock);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }


  @Test
  void givenInvalidUserForOrganizationIdWhenGetInstallmentRegistriesThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String nav = "nav";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        installmentRegistryRetrieverService.getInstallmentRegistries(organizationId, debtPositionId, nav, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(installmentRegistryServiceMock);
  }
}
