package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class SpontaneousFormControllerTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @Mock
  private SpontaneousFormRetrieverService spontaneousFormRetrieverServiceMock;
  @InjectMocks
  private SpontaneousFormController spontaneousFormController;

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormRetrieverServiceMock);
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetSpontaneousFormsThenOk() {
    long organizationId = 1L;
    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    Mockito.when(spontaneousFormRetrieverServiceMock.getSpontaneousForms(organizationId, loggedUser, accessToken)).thenReturn(expectedResult);

    ResponseEntity<List<SpontaneousForm>> response = spontaneousFormController.getSpontaneousForms(
        organizationId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPagedSpontaneousFormsThenOk() {
    long organizationId = 1L;
    String code = "code";
    Pageable pageable = PageRequest.ofSize(10);
    PagedSpontaneousForm expectedResult = podamFactory.manufacturePojo(PagedSpontaneousForm.class);

    Mockito.when(spontaneousFormRetrieverServiceMock.getPagedSpontaneousForms(organizationId, code, pageable, loggedUser, accessToken)).thenReturn(expectedResult);

    ResponseEntity<PagedSpontaneousForm> response = spontaneousFormController.getPagedSpontaneousForms(
        organizationId, code, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}