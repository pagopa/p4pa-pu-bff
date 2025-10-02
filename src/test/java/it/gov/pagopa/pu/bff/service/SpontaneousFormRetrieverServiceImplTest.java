package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormService spontaneousFormServiceMock;

  private SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    spontaneousFormRetrieverService = new SpontaneousFormRetrieverServiceImpl(spontaneousFormServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormServiceMock);
  }

  @Test
  void givenValidUserWhenGetSpontaneousFormsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;

    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.findAllByOrganizationId(organizationId,accessToken))
        .thenReturn(expectedResult);

      List<SpontaneousForm> result = spontaneousFormRetrieverService.getSpontaneousForms(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }
}