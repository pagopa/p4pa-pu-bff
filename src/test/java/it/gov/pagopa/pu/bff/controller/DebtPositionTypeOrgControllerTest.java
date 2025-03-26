package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgControllerTest {

  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;

  @InjectMocks
  private DebtPositionTypeOrgController debtPositionTypeOrgController;

  @BeforeEach
  void setUp() {
    UserInfo userInfo = new UserInfo();
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
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()
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
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()
    )).thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeOrgWithCount> response = debtPositionTypeOrgController.getDebtPositionTypeOrgWithCount(
      organizationId, code, description, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}


