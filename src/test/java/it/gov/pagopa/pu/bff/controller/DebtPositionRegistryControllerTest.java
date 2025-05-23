package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.debt_position_registry.DebtPositionRegistryRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.DebtPositionRegistry;
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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DebtPositionRegistryControllerTest {

  @Mock
  private DebtPositionRegistryRetrieverService debtPositionRegistryRetrieverServiceMock;

  @InjectMocks
  private DebtPositionRegistryController debtPositionRegistryController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      debtPositionRegistryRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionRegistriesThenOk() {
    long organizationId = 1L;
    long debtPositionId = 2L;
    List<DebtPositionRegistry> expectedResult = new ArrayList<>();
    expectedResult.add(new DebtPositionRegistry());

    Mockito.when(debtPositionRegistryRetrieverServiceMock.getDebtPositionRegistry(organizationId,debtPositionId, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<List<DebtPositionRegistry>> response = debtPositionRegistryController.getDebtPositionRegistries(organizationId, debtPositionId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}

