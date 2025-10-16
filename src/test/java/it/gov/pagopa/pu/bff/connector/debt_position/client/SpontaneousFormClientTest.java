package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.SpontaneousFormApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private SpontaneousFormApi spontaneousFormApiMock;

  SpontaneousFormClient spontaneousFormClient;

  @BeforeEach
  void setUp() {
    spontaneousFormClient = new SpontaneousFormClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
        spontaneousFormApiMock
    );
  }

  @Test
  void whenCreateSpontaneousFormThenInvokeWithAccessToken() {

    String accessToken = "ACCESSTOKEN";

    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);

    when(debtPositionApisHolderMock.getSpontaneousFormApi(accessToken))
      .thenReturn(spontaneousFormApiMock);
    when(spontaneousFormApiMock.createSpontaneousForm(expectedResult))
      .thenReturn(expectedResult);

    SpontaneousForm result = spontaneousFormClient.createSpontaneousForm(expectedResult, accessToken);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteSpontaneousFormThenInvokeWithAccessToken() {

    String accessToken = "ACCESSTOKEN";

    Long spontaneousFormId = 1L;

    when(debtPositionApisHolderMock.getSpontaneousFormApi(accessToken))
        .thenReturn(spontaneousFormApiMock);
    doNothing().when(spontaneousFormApiMock).deleteSpontaneousForm(spontaneousFormId);

    assertDoesNotThrow(()->spontaneousFormClient.deleteSpontaneousForm(spontaneousFormId, accessToken));
  }

  @Test
  void givenNotFoundWhenDeleteSpontaneousFormThenResourceNotFoundException() {

    String accessToken = "ACCESSTOKEN";

    Long spontaneousFormId = 1L;

    when(debtPositionApisHolderMock.getSpontaneousFormApi(accessToken))
        .thenReturn(spontaneousFormApiMock);
    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
        .when(spontaneousFormApiMock).deleteSpontaneousForm(spontaneousFormId);

    assertThrows(ResourceNotFoundException.class, ()->spontaneousFormClient.deleteSpontaneousForm(spontaneousFormId, accessToken));
  }
}
