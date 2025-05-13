package it.gov.pagopa.pu.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PrintPaymentNoticeControllerTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PrintPaymentNoticeRetrieverService printPaymentNoticeRetrieverServiceMock;

  @InjectMocks
  private PrintPaymentNoticeController printPaymentNoticeController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      printPaymentNoticeRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGenerateNoticeThenOk() {
    long organizationId = 1L;
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    FileResourceDTO fileResourceDTO = new FileResourceDTO();
    fileResourceDTO.setResource(new ByteArrayResource("PDF-DATA".getBytes()));
    fileResourceDTO.setFileName("filename");

    Mockito.when(printPaymentNoticeRetrieverServiceMock.generateNotice(organizationId, iuv, debtPositionDTO, loggedUser, accessToken))
      .thenReturn(fileResourceDTO);

    ResponseEntity<Resource> response = printPaymentNoticeController.generateNotice(organizationId, iuv, debtPositionDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(fileResourceDTO.getResource(), response.getBody());
    assertEquals(fileResourceDTO.getFileName(), response.getHeaders().getContentDisposition().getFilename());
  }
}
