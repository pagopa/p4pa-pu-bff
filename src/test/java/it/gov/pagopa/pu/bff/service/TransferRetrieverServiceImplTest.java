package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.TransferService;
import it.gov.pagopa.pu.bff.service.transfer.TransferRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransferEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.TransferResponse;
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
class TransferRetrieverServiceImplTest {

  @Mock
  private TransferService transferServiceMock;

  private TransferRetrieverServiceImpl transferRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    transferRetrieverService = new TransferRetrieverServiceImpl(transferServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(transferServiceMock);
  }

  @Test
  void givenValidUserWhenGetTransfersThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long installmentId = 1L;
    TransferResponse transferResponse = new TransferResponse();

    CollectionModelTransferEmbedded embedded = new CollectionModelTransferEmbedded();
    embedded.setTransfers(List.of(transferResponse));
    CollectionModelTransfer collectionModel = new CollectionModelTransfer();
    collectionModel.setEmbedded(embedded);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      Mockito.when(transferServiceMock.getTransfers(installmentId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(collectionModel);

      List<TransferResponse> result = transferRetrieverService.getTransfers(organizationId, installmentId, loggedUser, accessToken);

      assertNotNull(result);
      assertFalse(result.isEmpty());
      assertSame(transferResponse, result.get(0));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verify(transferServiceMock).getTransfers(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }

  @Test
  void givenEmptyResultWhenGetTransfersThenEmptyList() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long installmentId = 1L;
    CollectionModelTransfer emptyCollectionModel = new CollectionModelTransfer();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      Mockito.when(transferServiceMock.getTransfers(installmentId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(emptyCollectionModel);

      List<TransferResponse> result = transferRetrieverService.getTransfers(organizationId, installmentId, loggedUser, accessToken);

      assertNotNull(result);
      assertTrue(result.isEmpty());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
      Mockito.verify(transferServiceMock).getTransfers(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }

  @Test
  void givenInvalidUserWhenGetTransfersThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long installmentId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        transferRetrieverService.getTransfers(organizationId, installmentId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
    }
  }
}
