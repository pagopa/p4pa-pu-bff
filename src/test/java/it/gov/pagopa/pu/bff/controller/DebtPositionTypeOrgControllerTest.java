package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgControllerTest {

  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;

  @InjectMocks
  private DebtPositionTypeOrgController debtPositionTypeOrgController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      debtPositionTypeOrgRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgByIdThenOk() {
    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    DebtPositionTypeOrg expectedResult = new DebtPositionTypeOrg();

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgById(
      organizationId,
      debtPositionTypeOrgId,
      loggedUser, accessToken
    )).thenReturn(expectedResult);

    ResponseEntity<DebtPositionTypeOrg> response = debtPositionTypeOrgController.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenNonExistentDebtPositionTypeOrgWhenGetDebtPositionTypeOrgByIdThenNotFound() {
    long organizationId = 1L;
    long debtPositionTypeOrgId = 999L;

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgById(
      organizationId,
      debtPositionTypeOrgId,
      loggedUser, accessToken
    )).thenReturn(null);

    ResponseEntity<DebtPositionTypeOrg> response = debtPositionTypeOrgController.getDebtPositionTypeOrgById(organizationId, debtPositionTypeOrgId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgsThenOk() {
    long organizationId = 1L;
    List<DebtPositionTypeOrg> expectedResult = List.of(new DebtPositionTypeOrg());

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgs(
      organizationId,
      loggedUser, accessToken
    )).thenReturn(expectedResult);

    ResponseEntity<List<DebtPositionTypeOrg>> response = debtPositionTypeOrgController.getDebtPositionTypeOrgs(organizationId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgWithCountThenOk() {
    long organizationId = 1L;
    String code = "testCode";
    String description = "testDescription";
    Pageable pageable = PageRequest.of(0, 10);
    PagedDebtPositionTypeOrgWithCount expectedResult = new PagedDebtPositionTypeOrgWithCount();

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgWithCount(
      organizationId,
      code,
      description,
      pageable,
      loggedUser, accessToken
    )).thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeOrgWithCount> response = debtPositionTypeOrgController.getDebtPositionTypeOrgWithCount(
      organizationId, code, description, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgOperatorsThenOk() {
    long organizationId = 1L;
    long debtPositionTypeOrgId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    PagedDebtPositionTypeOrgOperatorDTO expectedResult = new PagedDebtPositionTypeOrgOperatorDTO();

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgOperators(
      organizationId,
      debtPositionTypeOrgId,
      pageable,
      loggedUser, accessToken
    )).thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeOrgOperatorDTO> response = debtPositionTypeOrgController.getDebtPositionTypeOrgOperators(
      organizationId, debtPositionTypeOrgId, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenDeleteDebtPositionTypeOrgThenOk() {
    long organizationId = 1L;
    long debtPositionTypeOrgId = 2L;

    Mockito.doNothing().when(debtPositionTypeOrgRetrieverServiceMock).deleteDebtPositionTypeOrg(
      Mockito.eq(organizationId),
      Mockito.eq(debtPositionTypeOrgId),
      Mockito.same(loggedUser), Mockito.same(accessToken)
    );

    ResponseEntity<Void> response = debtPositionTypeOrgController.deleteDebtPositionTypeOrg(
      organizationId, debtPositionTypeOrgId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

}


