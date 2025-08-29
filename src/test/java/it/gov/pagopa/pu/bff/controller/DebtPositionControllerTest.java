package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionNoticeRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.ManageDebtPositionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionControllerTest {

  @Mock
  private DebtPositionRetrieverService debtPositionRetrieverServiceMock;
  @Mock
  private DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverServiceMock;

  @InjectMocks
  private DebtPositionController debtPositionController;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      debtPositionRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenCreateDebtPositionThenOk() {
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    DebtPositionDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDTO.class);

    Mockito.when(debtPositionRetrieverServiceMock.createDebtPosition(
        Mockito.same(debtPositionDTO),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionDTO> response = debtPositionController.createDebtPosition(debtPositionDTO);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionViewsThenOk() {
    long organizationId = 1L;
    OffsetDateTime creationDateFrom = OffsetDateTime.now();
    OffsetDateTime creationDateTo = OffsetDateTime.now();
    String fiscalCode = "fiscalCode";
    Long debtPositionTypeOrgId = 2L;
    DebtPositionStatus status = DebtPositionStatus.REPORTED;

    PagedDebtPositionView expectedResult = podamFactory.manufacturePojo(PagedDebtPositionView.class);

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionViews(
        Mockito.argThat(f ->
          f.getOrganizationId().equals(organizationId)
            && f.getCreationDateTimeFrom().equals(creationDateFrom)
            && f.getCreationDateTimeTo().equals(creationDateTo)
            && f.getFiscalCode().equals(fiscalCode)
            && f.getDebtPositionTypeOrgId().equals(debtPositionTypeOrgId)
            && f.getStatus().equals(status)
        ),
        Mockito.argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 10 && p.getSort().isUnsorted()),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionView> response = debtPositionController.getDebtPositionViews(
      organizationId,
      creationDateFrom,
      creationDateTo,
      fiscalCode,
      debtPositionTypeOrgId,
      status,
      PageRequest.of(0, 10));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenExistingDebtPositionWhenGetDebtPositionDetailThenOk() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    DebtPositionDetailDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDetailDTO.class);

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionDetail(
        Mockito.same(debtPositionId),
        Mockito.same(organizationId),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionDetailDTO> response = debtPositionController.getDebtPositionDetail(
      organizationId,
      debtPositionId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenNoDebtPositionWhenGetDebtPositionDetailThenNotFound() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionDetail(
        Mockito.same(debtPositionId),
        Mockito.same(organizationId),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(null);

    ResponseEntity<DebtPositionDetailDTO> response = debtPositionController.getDebtPositionDetail(
      organizationId,
      debtPositionId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenDebtPositionIdWhenDeleteDebtPositionThenOk() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    Mockito.when(debtPositionRetrieverServiceMock.deleteDebtPosition(
        Mockito.same(organizationId),
        Mockito.same(debtPositionId),
        Mockito.same(loggedUser),
        Mockito.same(accessToken)))
      .thenReturn(false);

    ResponseEntity<Void> response = debtPositionController.deleteDebtPosition(
      organizationId,
      debtPositionId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenDebtPositionIdWhenDeleteDebtPositionThenNoContent() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    Mockito.when(debtPositionRetrieverServiceMock.deleteDebtPosition(
        Mockito.same(organizationId),
        Mockito.same(debtPositionId),
        Mockito.same(loggedUser),
        Mockito.same(accessToken)))
      .thenReturn(true);

    ResponseEntity<Void> response = debtPositionController.deleteDebtPosition(
      organizationId,
      debtPositionId);

    Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPaymentNoticeThenOk() {
    long organizationId = 1L;
    String iuv = "iuv";
    Long debtPositionId = 2L;
    FileResourceDTO fileResourceDTO = new FileResourceDTO();
    fileResourceDTO.setResource(new ByteArrayResource("PDF-DATA".getBytes()));
    fileResourceDTO.setFileName("filename");

    Mockito.when(debtPositionNoticeRetrieverServiceMock.getNotice(organizationId, iuv, debtPositionId, loggedUser, accessToken))
      .thenReturn(fileResourceDTO);

    ResponseEntity<Resource> response = debtPositionController.getPaymentNotice(organizationId, debtPositionId, iuv);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(fileResourceDTO.getResource(), response.getBody());
    assertEquals(fileResourceDTO.getFileName(), response.getHeaders().getContentDisposition().getFilename());
  }

  @Test
  void givenCorrectRequestWhenGetUnpaidPaymentNoticeZipThenOk() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    Resource resource = new ByteArrayResource("PDF-DATA".getBytes());

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken))
      .thenReturn(resource);

    ResponseEntity<Resource> response = debtPositionController.getUnpaidPaymentNoticeZip(organizationId, debtPositionId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(resource, response.getBody());
    assertEquals("1_2_NOTICES_PDF.zip", response.getHeaders().getContentDisposition().getFilename());
  }

  @Test
  void givenCorrectRequestWhenGetUnpaidPaymentNoticeZipThenNoContent() {
    long organizationId = 1L;
    Long debtPositionId = 2L;

    Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<Resource> response = debtPositionController.getUnpaidPaymentNoticeZip(organizationId, debtPositionId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenExistingDebtPositionWhenManageDebtPositionInstallmentsThenOk() {
    long organizationId = 1L;
    Long debtPositionId = 2L;
    ManageDebtPositionDTO manageDebtPositionDTO = podamFactory.manufacturePojo(ManageDebtPositionDTO.class);
    DebtPositionDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDTO.class);

    Mockito.when(debtPositionRetrieverServiceMock.manageDebtPositionInstallments(
                    organizationId,
                    debtPositionId,
                    manageDebtPositionDTO,true, loggedUser,accessToken))
            .thenReturn(expectedResult);

    ResponseEntity<DebtPositionDTO> response = debtPositionController.manageDebtPositionInstallments(
            organizationId,
            debtPositionId,
            manageDebtPositionDTO,true);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenNoDebtPositionWhenManageDebtPositionInstallmentsThenNotFound() {
    long organizationId = 1L;
    Long debtPositionId = 2L;
    ManageDebtPositionDTO manageDebtPositionDTO = podamFactory.manufacturePojo(ManageDebtPositionDTO.class);

    Mockito.when(debtPositionRetrieverServiceMock.manageDebtPositionInstallments(
                    organizationId,
                    debtPositionId,
                    manageDebtPositionDTO,true, loggedUser,accessToken))
            .thenReturn(null);

    ResponseEntity<DebtPositionDTO> response = debtPositionController.manageDebtPositionInstallments(
            organizationId,
            debtPositionId,
            manageDebtPositionDTO,true);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenExistingDebtPositionIdWhenPublishDebtPositionThenOk() {
    long organizationId = 1L;
    Long debtPositionId = 3L;
    DebtPositionDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDTO.class);

    Mockito.when(debtPositionRetrieverServiceMock.publishDebtPosition(organizationId, debtPositionId, loggedUser,accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionDTO> response = debtPositionController.publishDebtPosition(organizationId, debtPositionId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}
