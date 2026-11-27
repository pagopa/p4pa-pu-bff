package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.exception.common.ConflictException;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceViewMapper;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
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
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrgSilServiceRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSilServiceService orgSilServiceServiceMock;
  @Mock
  private OrgSilServiceDTOMapper orgSilServiceDTOMapperMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private OrgSilServiceViewMapper orgSilServiceViewMapperMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;

  private OrgSilServiceRetrieverService orgSilServiceRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    orgSilServiceRetrieverService = new OrgSilServiceRetrieverServiceImpl(
      orgSilServiceServiceMock, orgSilServiceDTOMapperMock, authorizationServiceMock, orgSilServiceViewMapperMock, debtPositionTypeOrgServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      orgSilServiceServiceMock, orgSilServiceDTOMapperMock, authorizationServiceMock, orgSilServiceViewMapperMock, debtPositionTypeOrgServiceMock
    );
  }

  @Test
  void givenValidUserWhenGetOrgSilServicesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    CollectionModelOrgSilService collectionModelOrgSilService = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);
    List<OrgSilServiceExtendedDTO> expectedResult = Collections.singletonList(new OrgSilServiceExtendedDTO());

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(orgSilServiceServiceMock.getOrgSilServices(organizationId, serviceType, accessToken))
        .thenReturn(collectionModelOrgSilService);
      when(orgSilServiceDTOMapperMock.map(collectionModelOrgSilService.getEmbedded().getOrgSilServices())).thenReturn(expectedResult);

      List<OrgSilServiceExtendedDTO> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

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

    Long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    CollectionModelOrgSilService collectionModelOrgSilService = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);
    collectionModelOrgSilService.setEmbedded(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(orgSilServiceServiceMock.getOrgSilServices(organizationId, serviceType, accessToken))
        .thenReturn(collectionModelOrgSilService);

      List<OrgSilServiceExtendedDTO> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

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

    Long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(orgSilServiceServiceMock.getOrgSilServices(organizationId, serviceType, accessToken))
        .thenReturn(null);

      List<OrgSilServiceExtendedDTO> result = orgSilServiceRetrieverService.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken);

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

    Long organizationId = 1L;
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

  @Test
  void givenNullServiceIdWhenGetOrgSilServiceApplicationNameThenReturnNull() {
    String result = orgSilServiceRetrieverService.getOrgSilServiceApplicationName(null, accessToken);
    assertNull(result);
  }

  @Test
  void givenValidServiceIdButServiceNotFoundWhenGetOrgSilServiceApplicationNameThenReturnNull() {
    when(orgSilServiceServiceMock.getOrgSilServiceById(1L, accessToken))
      .thenReturn(null);

    String result = orgSilServiceRetrieverService.getOrgSilServiceApplicationName(1L, accessToken);
    assertNull(result);
  }

  @Test
  void givenValidServiceIdWhenGetOrgSilServiceApplicationNameThenReturnApplicationName() {
    OrgSilService service = new OrgSilService();
    service.setApplicationName("TestApp");

    when(orgSilServiceServiceMock.getOrgSilServiceById(1L, accessToken))
      .thenReturn(service);

    String result = orgSilServiceRetrieverService.getOrgSilServiceApplicationName(1L, accessToken);
    assertEquals("TestApp", result);
  }

  @Test
  void givenValidAdminWhenGetOrgSilServicesByFiltersThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("admin-123");

    Long organizationId = 1L;
    String applicationName = "appName";
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    Boolean flagLegacy = true;
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelOrgSilServiceView pagedModel = new PagedModelOrgSilServiceView();
    PagedOrgSilServiceView mappedView = new PagedOrgSilServiceView();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    when(orgSilServiceServiceMock.getOrgSilServicesByFilters(
      organizationId, applicationName, serviceType, flagLegacy, pageable, accessToken))
      .thenReturn(pagedModel);

    when(orgSilServiceViewMapperMock.map(pagedModel)).thenReturn(mappedView);

    PagedOrgSilServiceView result = orgSilServiceRetrieverService.getOrgSilServicesByFilters(
      organizationId, applicationName, serviceType, flagLegacy, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(mappedView, result);
  }

  @Test
  void givenNonAdminWhenGetOrgSilServicesByFiltersThenUnauthorized() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-456");

    Long organizationId = 1L;
    String applicationName = "appName";
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    Boolean flagLegacy = true;
    Pageable pageable = PageRequest.of(0, 10);

    doThrow(new AuthorizationDeniedException("Access denied"))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    assertThrows(AuthorizationDeniedException.class, () ->
      orgSilServiceRetrieverService.getOrgSilServicesByFilters(
        organizationId, applicationName, serviceType, flagLegacy, pageable, loggedUser, accessToken));

    verifyNoInteractions(orgSilServiceServiceMock);
    verifyNoInteractions(orgSilServiceViewMapperMock);
  }

  @Test
  void givenValidOrgSilServiceIdWhenGetOrgSilServiceDetailsThenReturnOrgSilServiceDTO() {
    OrgSilServiceDTO orgSilServiceDTO = new OrgSilServiceDTO();
    OrgSilServiceDecryptedDTO orgSilServiceExtended = new OrgSilServiceDecryptedDTO();
    Long organizationId = 1L;

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    when(orgSilServiceServiceMock.getOrgSilServiceByIdDecrypted(2L, accessToken)).thenReturn(orgSilServiceDTO);
    when(orgSilServiceDTOMapperMock.map(orgSilServiceDTO)).thenReturn(orgSilServiceExtended);

    OrgSilServiceDecryptedDTO result = orgSilServiceRetrieverService.getOrgSilServiceDetails(organizationId, 2L, loggedUser, accessToken);

    assertNotNull(result);
    assertEquals(orgSilServiceExtended, result);

  }

  @Test
  void givenInvalidAdminForOrganizationIdWhenGetOrgSilServiceDetailsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId = 1L;

    doThrow(AuthorizationDeniedException.class).when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      orgSilServiceRetrieverService.getOrgSilServiceDetails(organizationId, 2L, loggedUser, accessToken));

  }

  @Test
  void givenValidInputWhenDeleteOrgSilServiceThenDeleteOrgSilService() {
    Long organizationId = 1L;
    Long orgSilServiceId = 2L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("external-123");

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    when(debtPositionTypeOrgServiceMock.countByOrgSilServiceId(orgSilServiceId, accessToken)).thenReturn(0L);
    doNothing().when(orgSilServiceServiceMock).deleteOrgSilService(orgSilServiceId, accessToken);

    orgSilServiceRetrieverService.deleteOrgSilService(organizationId, orgSilServiceId, loggedUser, accessToken);
  }

  @Test
  void givenUtilizedOrgSilServiceIdWhenDeleteThenThrowConflictException() {
    Long organizationId = 1L;
    Long orgSilServiceId = 2L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("external-123");

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    when(debtPositionTypeOrgServiceMock.countByOrgSilServiceId(orgSilServiceId, accessToken)).thenReturn(3L); // > 0

    ConflictException exception = assertThrows(ConflictException.class,
      () -> orgSilServiceRetrieverService.deleteOrgSilService(organizationId, orgSilServiceId, loggedUser, accessToken));

    assertEquals("Cannot delete OrgSilService with ID 2: it is referenced by 3 DebtPositionTypeOrg record(s).", exception.getMessage());
  }

  @Test
  void givenInvalidAdminForOrganizationIdWhenDeleteOrgSilServiceThenThrowAuthorizationDeniedException() {
    Long organizationId = 1L;
    Long orgSilServiceId = 2L;

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("external-123");

    doThrow(AuthorizationDeniedException.class).when(authorizationServiceMock)
      .validateAdminRole(organizationId, loggedUser);

    assertThrows(AuthorizationDeniedException.class,
      () -> orgSilServiceRetrieverService.deleteOrgSilService(organizationId, orgSilServiceId, loggedUser, accessToken));
  }
}
