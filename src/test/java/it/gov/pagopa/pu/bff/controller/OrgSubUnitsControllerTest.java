package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.org_sub_unit.OrgSubUnitRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitsControllerTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrgSubUnitRetrieverService subUnitRetrieverServiceMock;

  @InjectMocks
  private OrgSubUnitsController orgSubUnitsController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(subUnitRetrieverServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenCreateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(subUnitRetrieverServiceMock.createOrgSubUnit(organizationId, requestBody, loggedUser, accessToken))
      .thenReturn(expectedResult);

    // When
    ResponseEntity<OrgSubUnit> response = orgSubUnitsController.createOrgSubUnit(organizationId, requestBody);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenDeleteOrgSubUnitByIdThenOk() {
    // Given
    Long organizationId = 1L;
    String orgSubUnitId = "SUB_UNIT_ID";

    // When
    ResponseEntity<Void> response = orgSubUnitsController.deleteOrgSubUnitById(organizationId, orgSubUnitId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
    verify(subUnitRetrieverServiceMock).deleteOrgSubUnit(organizationId, orgSubUnitId, loggedUser, accessToken);
  }

  @Test
  void givenCorrectRequestWhenGetOrgSubUnitByIdThenOk() {
    // Given
    Long organizationId = 1L;
    String orgSubUnitId = "SUB_UNIT_ID";
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(subUnitRetrieverServiceMock.getOrgSubUnitById(organizationId, orgSubUnitId, loggedUser, accessToken))
      .thenReturn(expectedResult);

    // When
    ResponseEntity<OrgSubUnit> response = orgSubUnitsController.getOrgSubUnitById(organizationId, orgSubUnitId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenUpdateOrgSubUnitThenOk() {
    // Given
    Long organizationId = 1L;
    String orgSubUnitId = "SUB_UNIT_ID";
    OrgSubUnitRequestBody requestBody = podamFactory.manufacturePojo(OrgSubUnitRequestBody.class);
    OrgSubUnit expectedResult = podamFactory.manufacturePojo(OrgSubUnit.class);

    when(subUnitRetrieverServiceMock.updateOrgSubUnit(organizationId, orgSubUnitId, requestBody, loggedUser, accessToken))
      .thenReturn(expectedResult);

    // When
    ResponseEntity<OrgSubUnit> response = orgSubUnitsController.updateOrgSubUnit(organizationId, orgSubUnitId, requestBody);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenUpdateOrgSubUnitStatusThenOk() {
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus orgSubUnitStatus = OrgSubUnitStatus.CANCELLED;

    ResponseEntity<Void> response = orgSubUnitsController.updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
    verify(subUnitRetrieverServiceMock).updateOrgSubUnitStatus(organizationId, subUnitCode, orgSubUnitStatus, loggedUser, accessToken);
  }

  @Test
  void givenCorrectRequestWhenGetPagedOrgSubUnitsThenOk() {
    Long organizationId = 1L;
    String mappedExternalUserId = "mappedExternalUserId";
    String subUnitCode = "subUnitCode";
    OrgSubUnitStatus status = OrgSubUnitStatus.ACTIVE;
    SubUnitType subUnitType = SubUnitType.UO;

    Pageable pageable = PageRequest.ofSize(10);

    PagedOrgSubUnit expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnit.class);

    when(
      subUnitRetrieverServiceMock.getPagedOrgSubUnits(
        Mockito.any(PagedOrgSubUnitFiltersDTO.class),
        Mockito.eq(pageable),
        Mockito.eq(loggedUser),
        Mockito.eq(accessToken)
      )
    ).thenReturn(expectedResult);

    ResponseEntity<PagedOrgSubUnit> response = orgSubUnitsController.getPagedOrgSubUnits(
      organizationId,
      mappedExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable
    );

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}
