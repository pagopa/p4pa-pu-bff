package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgControllerTest {

  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;

  @InjectMocks
  private DebtPositionTypeOrgController debtPositionTypeOrgController;

  private UserInfo userInfo;

  @BeforeEach
  void setUp() {
    userInfo = new UserInfo();
    userInfo.setMappedExternalUserId("fakeExternalUser");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgsThenOk() {
    long organizationId = 1L;
    List<DebtPositionTypeOrg> expectedResult = List.of(new DebtPositionTypeOrg());

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgs(
      organizationId,
      userInfo.getMappedExternalUserId(),
      userInfo,
      "fakeAccessToken"
    )).thenReturn(expectedResult);

    ResponseEntity<List<DebtPositionTypeOrg>> response = debtPositionTypeOrgController.getDebtPositionTypeOrgs(organizationId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenEmptyResultWhenGetDebtPositionTypeOrgsThenNoContent() {
    long organizationId = 1L;
    List<DebtPositionTypeOrg> emptyResult = Collections.emptyList();

    Mockito.when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgs(
      organizationId,
      userInfo.getMappedExternalUserId(),
      userInfo,
      "fakeAccessToken"
    )).thenReturn(emptyResult);

    ResponseEntity<List<DebtPositionTypeOrg>> response = debtPositionTypeOrgController.getDebtPositionTypeOrgs(organizationId);

    Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }

}

