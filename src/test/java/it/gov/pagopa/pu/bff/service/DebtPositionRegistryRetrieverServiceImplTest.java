package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.DebtPositionRegistryService;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        List<DebtPositionRegistry> expectedResult = collectionModelDebtPositionRegistry.getEmbedded().getDebtPositionRegistries();

        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        Mockito.when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId,accessToken))
                .thenReturn(collectionModelDebtPositionRegistry);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertSame(expectedResult, result);

        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);
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

        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        Mockito.when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId,accessToken))
                .thenReturn(collectionModelDebtPositionRegistry);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);

    }

    @Test
    void givenNullDebtPositionRegistryCollectionWhenGetDebtPositionRegistryThenEmptyList() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;

        Mockito.doNothing().when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);
        Mockito.when(debtPositionRegistryServiceMock.findDebtPositionRegistries(debtPositionId,accessToken))
                .thenReturn(null);

        List<DebtPositionRegistry> result = debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        Mockito.verifyNoMoreInteractions(debtPositionRegistryServiceMock);

    }

    @Test
    void givenInvalidUserWhenGetDebtPositionRegistryThenAuthorizationDeniedException() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        Long debtPositionId=2L;

      Mockito.doThrow(new AuthorizationDeniedException("Access denied")).when(debtPositionRetrieverServiceMock).validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
              debtPositionRegistryRetrieverService.getDebtPositionRegistry(organizationId, debtPositionId, loggedUser, accessToken));

        Mockito.verifyNoInteractions(debtPositionRegistryServiceMock);
    }
}
