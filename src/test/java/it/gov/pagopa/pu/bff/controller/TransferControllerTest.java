package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.transfer.TransferRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.Transfer;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

  @Mock
  private TransferRetrieverService transferRetrieverServiceMock;

  @InjectMocks
  private TransferController transferController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      transferRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetTransfersThenOk() {
    long organizationId = 1L;
    long installmentId = 1L;
    List<Transfer> expectedResult = List.of(new Transfer());

    Mockito.when(transferRetrieverServiceMock.getTransfers(
      organizationId,
      installmentId,
      loggedUser, accessToken
    )).thenReturn(expectedResult);

    ResponseEntity<List<Transfer>> response = transferController.getTransfers(organizationId, installmentId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCategoryWhenValidateTaxonomyCategoryThenOk() {
    String category = "001122233";
    String orgFiscalCode = "orgFiscalCode";
    Boolean expectedResult = true;

    Mockito.when(transferRetrieverServiceMock.validateTaxonomyCategory(category, orgFiscalCode, accessToken))
      .thenReturn(true);

    ResponseEntity<Boolean> response = transferController.validateTaxonomyCategory(orgFiscalCode, category);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}
