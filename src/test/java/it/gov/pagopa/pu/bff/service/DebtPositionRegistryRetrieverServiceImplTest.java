package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.DebtPositionRegistryService;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position_registry.DebtPositionRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position_registry.DebtPositionRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebtPositionRegistryRetrieverServiceImplTest {

    public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
    @Mock
    private DebtPositionRegistryService debtPositionRegistryServiceMock;
    @Mock
    private DebtPositionRetrieverService debtPositionRetrieverServiceMock;

    private DebtPositionRegistryRetrieverService debtPositionRegistryRetrieverService;

    private final String accessToken = "TOKEN";

    @BeforeEach
    void setUp() {
        debtPositionRegistryRetrieverService = new DebtPositionRegistryRetrieverServiceImpl(debtPositionRegistryServiceMock, debtPositionRetrieverServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions(){
        Mockito.verifyNoMoreInteractions(
                debtPositionRegistryServiceMock,
                debtPositionRetrieverServiceMock
        );
    }

    @Test
    void givenValidUserAndValidDebtPositionIdWhenGetDebtPositionRegistryThenOk() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;
        CollectionModelDebtPositionRegistry collectionModelDebtPositionRegistry = podamFactory.manufacturePojo(CollectionModelDebtPositionRegistry.class);
        List<DebtPositionRegistry> expectedResult = Objects.requireNonNull(collectionModelDebtPositionRegistry.getEmbedded()).getDebtPositionRegistries();

      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId, accessToken))
          .thenReturn(collectionModelDebtPositionRegistry);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertSame(expectedResult, result);

        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);
      }
    }

    @Test
    void givenNullEmbeddedDebtPositionRegistryCollectionWhenGetDebtPositionRegistryThenEmptyList() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;
        CollectionModelDebtPositionRegistry collectionModelDebtPositionRegistry = podamFactory.manufacturePojo(CollectionModelDebtPositionRegistry.class);
        collectionModelDebtPositionRegistry.setEmbedded(null);

      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId, accessToken))
          .thenReturn(collectionModelDebtPositionRegistry);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);
        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }

    }

    @Test
    void givenNullDebtPositionRegistryCollectionWhenGetDebtPositionRegistryThenEmptyList() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;
      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId,accessToken))
                .thenReturn(null);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);
        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }

    }

    @Test
    void givenInvalidUserWhenGetDebtPositionRegistryThenResourceNotFoundException() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;
      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
        doThrow(new NotFoundException("DEBT_POSITION_NOT_FOUND", "DebtPosition not found"))
          .when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

        Assertions.assertThrows(NotFoundException.class, () ->
          debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken));

        Mockito.verifyNoInteractions(debtPositionRegistryServiceMock);
        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }
    }


  @Test
  void givenInvalidUserForOrganizationIdWhenGetDebtPositionRegistryThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(debtPositionRegistryServiceMock);
  }
}
