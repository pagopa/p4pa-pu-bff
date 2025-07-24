package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSilServiceService;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.mapper.OrgSilServiceDTOMapper;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceStorerService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceStorerServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyBasicAuthConfigDTO;
import it.gov.pagopa.pu.organization.dto.generated.SilServiceLegacyJwtAuthConfigDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrgSilServiceStorerServiceImplTest {
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSilServiceService orgSilServiceServiceMock;
  @Mock
  private OrgSilServiceDTOMapper orgSilServiceDTOMapperMock;
  @Mock
  private AuthorizationService authorizationServiceMock;

  private OrgSilServiceStorerService orgSilServiceStorerService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    orgSilServiceStorerService = new OrgSilServiceStorerServiceImpl(
      orgSilServiceServiceMock, orgSilServiceDTOMapperMock, authorizationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      orgSilServiceServiceMock, orgSilServiceDTOMapperMock, authorizationServiceMock
    );
  }

  @Test
  void givenOrgSilServiceIdNotNullWhenCreateThenThrowBadRequest() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setOrgSilServiceId(99L);
    UserInfo loggedUser = new UserInfo();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
      orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertTrue(ex.getReason().contains("orgSilServiceId must not be provided"));
  }

  @Test
  void givenFlagLegacyFalseAndAuthConfigProvidedWhenCreateThenThrowBadRequest() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setFlagLegacy(false);
    body.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());
    UserInfo loggedUser = new UserInfo();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
      orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertTrue(ex.getReason().contains("authConfig must not be provided"));
  }

  @Test
  void givenFlagLegacyTrueAndNoAuthConfigWhenCreateThenThrowBadRequest() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setFlagLegacy(true);
    UserInfo loggedUser = new UserInfo();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
      orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertTrue(ex.getReason().contains("exactly one authConfig"));
  }

  @Test
  void givenFlagLegacyTrueAndBothAuthConfigWhenCreateThenThrowBadRequest() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setFlagLegacy(true);
    body.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());
    body.setLegacyJwtAuthConfig(new SilServiceLegacyJwtAuthConfigDTO());
    UserInfo loggedUser = new UserInfo();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
      orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertTrue(ex.getReason().contains("exactly one authConfig"));
  }

  @Test
  void givenFlagLegacyFalseAndNoAuthConfigWhenCreateThenOk() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setFlagLegacy(false);
    UserInfo loggedUser = new UserInfo();

    OrgSilServiceDTO mappedDto = new OrgSilServiceDTO();
    OrgSilServiceDTO createdDto = new OrgSilServiceDTO();
    OrgSilServiceDecryptedDTO expectedResponse = new OrgSilServiceDecryptedDTO();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    when(orgSilServiceDTOMapperMock.toOrgSilServiceDTO(body)).thenReturn(mappedDto);
    when(orgSilServiceServiceMock.createOrUpdateOrgSilService(mappedDto, accessToken)).thenReturn(createdDto);
    when(orgSilServiceDTOMapperMock.map(createdDto)).thenReturn(expectedResponse);

    OrgSilServiceDecryptedDTO result = orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken);

    assertNotNull(result);
    assertEquals(expectedResponse, result);
  }

  @Test
  void givenFlagLegacyTrueAndOnlyBasicAuthConfigWhenCreateThenOk() {
    Long organizationId = 1L;
    OrgSilServiceDecryptedDTO body = new OrgSilServiceDecryptedDTO();
    body.setFlagLegacy(true);
    body.setLegacyBasicAuthConfig(new SilServiceLegacyBasicAuthConfigDTO());
    UserInfo loggedUser = new UserInfo();

    OrgSilServiceDTO mappedDto = new OrgSilServiceDTO();
    OrgSilServiceDTO createdDto = new OrgSilServiceDTO();
    OrgSilServiceDecryptedDTO expectedResponse = new OrgSilServiceDecryptedDTO();

    doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    when(orgSilServiceDTOMapperMock.toOrgSilServiceDTO(body)).thenReturn(mappedDto);
    when(orgSilServiceServiceMock.createOrUpdateOrgSilService(mappedDto, accessToken)).thenReturn(createdDto);
    when(orgSilServiceDTOMapperMock.map(createdDto)).thenReturn(expectedResponse);

    OrgSilServiceDecryptedDTO result = orgSilServiceStorerService.createOrgSilService(organizationId, body, loggedUser, accessToken);

    assertNotNull(result);
    assertEquals(expectedResponse, result);
  }
}
