package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrgSubUnitService orgSubUnitServiceMock;
  @Mock
  private AuthorizationService authorizationServiceMock;

  private OrgSubUnitRetrieverService orgSubUnitRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    orgSubUnitRetrieverService = new OrgSubUnitRetrieverServiceImpl(orgSubUnitServiceMock, authorizationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSubUnitServiceMock, authorizationServiceMock);
  }


  @Test
  void givenValidIdWhenGetOrgSubUnitByIdThenReturnSubUnit() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitRetrieverService.getOrgSubUnitById(orgSubUnitId, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenNonExistentIdWhenGetOrgSubUnitByIdThenThrowResourceNotFoundException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";

    when(orgSubUnitServiceMock.getOrgSubUnitById(orgSubUnitId, accessToken))
      .thenReturn(null);

    // When & Then
    ResourceNotFoundException exception = assertThrows(
      ResourceNotFoundException.class,
      () -> orgSubUnitRetrieverService.getOrgSubUnitById(orgSubUnitId, accessToken)
    );

    assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
    assertEquals("Organization SubUnit having orgSubUnitId " + orgSubUnitId + " not found", exception.getMessage());
  }

  @Test
  void givenValidUserWhenCreateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.createOrgSubUnit(requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitRetrieverService.createOrgSubUnit(organizationId, requestBody, loggedUser, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

  @Test
  void givenValidUserWhenDeleteOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    String orgSubUnitId = "SUB_UNIT_ID";
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    // When & Then
    assertDoesNotThrow(() -> orgSubUnitRetrieverService.deleteOrgSubUnit(organizationId, orgSubUnitId, loggedUser, accessToken));

    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    verify(orgSubUnitServiceMock).deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Test
  void givenValidUserWhenUpdateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    String orgSubUnitId = "SUB_UNIT_ID";
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(orgSubUnitServiceMock.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitRetrieverService.updateOrgSubUnit(organizationId, orgSubUnitId, requestBody, loggedUser, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
    verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
  }

}
