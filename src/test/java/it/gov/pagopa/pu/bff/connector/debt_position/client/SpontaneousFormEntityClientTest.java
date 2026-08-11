package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.client.generated.SpontaneousFormEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormEntityClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private SpontaneousFormEntityControllerApi spontaneousFormEntityControllerApiMock;

  SpontaneousFormEntityClient spontaneousFormEntityClient;

  @BeforeEach
  void setUp() {
    spontaneousFormEntityClient = new SpontaneousFormEntityClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      spontaneousFormEntityControllerApiMock
    );
  }

  @Test
  void givenSpontaneousFormIdWhenGetSpontaneousFormThenReturnSpontaneousForm() {
    //given
    String accessToken = "ACCESSTOKEN";
    Long spontaneousFormId = 1L;

    SpontaneousForm expectedResult = new SpontaneousForm();

    when(debtPositionApisHolderMock.getSpontaneousFormEntityControllerApi(accessToken))
      .thenReturn(spontaneousFormEntityControllerApiMock);
    when(spontaneousFormEntityControllerApiMock.crudGetSpontaneousform(String.valueOf(spontaneousFormId)))
      .thenReturn(expectedResult);
    //when
    SpontaneousForm result = spontaneousFormEntityClient.getSpontaneousForm(spontaneousFormId, accessToken);
    //then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void givenExceptionWhenGetSpontaneousFormThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long spontaneousFormId = 1L;

    when(debtPositionApisHolderMock.getSpontaneousFormEntityControllerApi(accessToken))
      .thenReturn(spontaneousFormEntityControllerApiMock);
    when(spontaneousFormEntityControllerApiMock.crudGetSpontaneousform(String.valueOf(spontaneousFormId)))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    SpontaneousForm result = spontaneousFormEntityClient.getSpontaneousForm(spontaneousFormId, accessToken);

    Assertions.assertNull(result);
  }

}
