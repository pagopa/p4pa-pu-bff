package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.transfer.TransferRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.TransferResponse;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

  @Mock
  private TransferRetrieverService transferRetrieverServiceMock;

  @InjectMocks
  private TransferController transferController;

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
  void givenCorrectRequestWhenGetTransfersThenOk() {
    long organizationId = 1L;
    long installmentId = 1L;
    List<TransferResponse> expectedResult = List.of(new TransferResponse());

    Mockito.when(transferRetrieverServiceMock.getTransfers(
      organizationId,
      installmentId,
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()
    )).thenReturn(expectedResult);

    ResponseEntity<List<TransferResponse>> response = transferController.getTransfers(organizationId, installmentId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}
