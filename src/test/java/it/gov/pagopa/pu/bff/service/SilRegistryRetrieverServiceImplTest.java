package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverServiceImpl;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ExtendWith(MockitoExtension.class)
class SilRegistryRetrieverServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;

  @Mock
  private SilRegistryService silRegistryServiceMock;

  @Mock
  private OrganizationService organizationServiceMock;

  private SilRegistryRetrieverService silRegistryRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    silRegistryRetrieverService = new SilRegistryRetrieverServiceImpl(
      authorizationServiceMock,
      organizationServiceMock,
      silRegistryServiceMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock,
      organizationServiceMock,
      silRegistryServiceMock
    );
  }

  @Test
  void givenValidUserWhenGetSilRegistryThenOk() {
    long organizationId = 1L;
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setBrokerId(10L);

    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    organization.setBrokerId(10L);
    organization.setOrgFiscalCode("ORG123");

    SilRegistryDTO expectedDTO = new SilRegistryDTO();
    expectedDTO.setRegistryId(registryId);
    expectedDTO.setOrgFiscalCode("ORG123");

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(organization);
    Mockito.when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(expectedDTO);

    SilRegistryDTO result = silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedDTO, result);

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
    Mockito.verify(silRegistryServiceMock).getSilRegistry(registryId, accessToken);
  }

  @Test
  void givenNoOrganizationWhenGetSilRegistryThenResourceNotFoundException() {
    long organizationId = 1L;
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(10L);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
    Mockito.verifyNoInteractions(silRegistryServiceMock);
  }

  @Test
  void givenMismatchedBrokerWhenGetSilRegistryThenResourceNotFoundException() {
    long organizationId = 1L;
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(10L);

    Organization organization = new Organization();
    organization.setBrokerId(99L);

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(organization);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
    Mockito.verifyNoInteractions(silRegistryServiceMock);
  }

  @Test
  void givenMismatchedFiscalCodeWhenGetSilRegistryThenResourceNotFoundException() {
    long organizationId = 1L;
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setBrokerId(10L);

    Organization organization = new Organization();
    organization.setBrokerId(10L);
    organization.setOrgFiscalCode("ORG123");

    SilRegistryDTO silRegistry = new SilRegistryDTO();
    silRegistry.setRegistryId(registryId);
    silRegistry.setOrgFiscalCode("DIFFERENT");

    Mockito.doNothing().when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId, accessToken)).thenReturn(organization);
    Mockito.when(silRegistryServiceMock.getSilRegistry(registryId, accessToken)).thenReturn(silRegistry);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verify(organizationServiceMock).getOrganizationByOrganizationId(organizationId, accessToken);
    Mockito.verify(silRegistryServiceMock).getSilRegistry(registryId, accessToken);
  }

  @Test
  void givenInvalidUserWhenGetSilRegistryThenAuthorizationDeniedException() {
    long organizationId = 1L;
    String registryId = "123";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-unauthorized");

    Mockito.doThrow(new AuthorizationDeniedException("Access denied"))
      .when(authorizationServiceMock).validateBrokerAdminRole(loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      silRegistryRetrieverService.getSilRegistry(organizationId, registryId, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateBrokerAdminRole(loggedUser);
    Mockito.verifyNoInteractions(organizationServiceMock, silRegistryServiceMock);
  }
}
