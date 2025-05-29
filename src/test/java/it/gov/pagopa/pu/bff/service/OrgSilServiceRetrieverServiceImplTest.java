package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
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

@ExtendWith(MockitoExtension.class)
public class OrgSilServiceRetrieverServiceImplTest {

    public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
    @Mock
    private OrgSilServiceService orgSilServiceServiceMock;

    private OrgSilServiceRetrieverService orgSilServiceRetrieverService;

    private final String accessToken = "TOKEN";

    @BeforeEach
    void setUp() {
        orgSilServiceRetrieverService = new OrgSilServiceRetrieverServiceImpl(orgSilServiceServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions(){
        Mockito.verifyNoMoreInteractions(
                orgSilServiceServiceMock
        );
    }

    @Test
    void givenValidUserWhenGetOrgSilServicesThenOk() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
        CollectionModelOrgSilService collectionModelOrgSilService = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);
        List<OrgSilService> expectedResult = collectionModelOrgSilService.getEmbedded().getOrgSilServices();

      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
        Mockito.when(orgSilServiceServiceMock.getOrgSilServices(organizationId,serviceType, accessToken))
          .thenReturn(collectionModelOrgSilService);

        List<OrgSilService> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

        assertNotNull(result);
        assertSame(expectedResult, result);

        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }
    }

    @Test
    void givenNullEmbeddedOrgSilServiceCollectionWhenGetOrgSilServicesThenEmptyList() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
        CollectionModelOrgSilService collectionModelOrgSilService = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);
        collectionModelOrgSilService.setEmbedded(null);

      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
          Mockito.when(orgSilServiceServiceMock.getOrgSilServices(organizationId,serviceType, accessToken))
                  .thenReturn(collectionModelOrgSilService);

          List<OrgSilService> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }
    }

    @Test
    void givenNullOrgSilServiceCollectionWhenGetOrgSilServicesThenEmptyList() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;

      try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
        authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
          Mockito.when(orgSilServiceServiceMock.getOrgSilServices(organizationId,serviceType, accessToken))
                  .thenReturn(null);

          List<OrgSilService> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result));

        authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      }
    }

    @Test
    void givenInvalidUserForOrganizationIdWhenGetOrgSilServicesThenAuthorizationDeniedException() {
        UserInfo loggedUser = new UserInfo();
        loggedUser.setUserId("user-123");
        loggedUser.setMappedExternalUserId("operatorExternalUserId");

        Long organizationId=1L;
        OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;

        try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
          authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
            .thenThrow(new AuthorizationDeniedException("Access denied"));

          Assertions.assertThrows(AuthorizationDeniedException.class, () ->
            orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken));

          authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
        }
        Mockito.verifyNoInteractions(orgSilServiceServiceMock);
    }
}
