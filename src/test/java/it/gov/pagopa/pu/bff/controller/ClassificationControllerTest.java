package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
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

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationControllerTest {

  @Mock
  private ClassificationRetrieverService classificationRetrieverServiceMock;

  @InjectMocks
  private ClassificationController classificationController;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationRetrieverServiceMock);
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetTreasuredClassificationThenOK() {
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = podamFactory.manufacturePojo(TreasuredClassificationFiltersDTO.class);
    treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(null);
    treasuredClassificationFiltersDTO.getLastClassificationDate().getFrom().atStartOfDay(ZoneId.systemDefault());

    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification mockPagedTreasuredClassification = new PagedTreasuredClassification();
    when(classificationRetrieverServiceMock.getTreasuredClassification(
      organizationId, treasuredClassificationFiltersDTO, debtPositionTypeOrgCode, pageable, loggedUser, accessToken))
      .thenReturn(mockPagedTreasuredClassification);

    ResponseEntity<PagedTreasuredClassification> response = classificationController.getTreasuredClassifications(organizationId,
      treasuredClassificationFiltersDTO.getLabel(),
      treasuredClassificationFiltersDTO.getIud(),
      treasuredClassificationFiltersDTO.getIuv(),
      treasuredClassificationFiltersDTO.getIur(),
      treasuredClassificationFiltersDTO.getLastClassificationDate().getFrom().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getLastClassificationDate().getTo().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getPayDate().getFrom().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getPayDate().getTo().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getPaymentDateTime().getFrom(),
      treasuredClassificationFiltersDTO.getPaymentDateTime().getTo(),
      treasuredClassificationFiltersDTO.getRegulationDate().getFrom().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getRegulationDate().getTo().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getBillDate().getFrom().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getBillDate().getTo().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getRegionValueDate().getFrom().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getRegionValueDate().getTo().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime(),
      treasuredClassificationFiltersDTO.getPspCompanyName(),
      treasuredClassificationFiltersDTO.getPspLastName(),
      treasuredClassificationFiltersDTO.getIuf(),
      treasuredClassificationFiltersDTO.getRegulationUniqueIdentifier(),
      treasuredClassificationFiltersDTO.getAccountRegistryCode(),
      treasuredClassificationFiltersDTO.getBillAmountCents(),
      treasuredClassificationFiltersDTO.getRemittanceInformation(),
      treasuredClassificationFiltersDTO.getDebtorFiscalCode(),
      debtPositionTypeOrgCode,
      treasuredClassificationFiltersDTO.getBillYear(),
      treasuredClassificationFiltersDTO.getBillCode(),
      treasuredClassificationFiltersDTO.getDocumentYear(),
      treasuredClassificationFiltersDTO.getDocumentCode(),
      treasuredClassificationFiltersDTO.getProvisionalAe(),
      treasuredClassificationFiltersDTO.getProvisionalCode(),
      pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockPagedTreasuredClassification, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetClassificationDetailThenOK() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailDTO mockDetailView = new ClassificationDetailDTO();
    when(classificationRetrieverServiceMock.getClassificationDetail(
      organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(mockDetailView);

    ResponseEntity<ClassificationDetailDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockDetailView, response.getBody());
  }

  @Test
  void givenIncorrectRequestWhenGetClassificationDetailThenNotFound() {
    long organizationId = 1L;
    long classificationId = 999L;

    when(classificationRetrieverServiceMock.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<ClassificationDetailDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPaidInstallmentsThenOK() {
    Long organizationId = 1L;
    Long assessmentId = 2L;
    String debtPositionTypeOrgCode = "TEST_CODE";
    String iuv = "IUV12345";
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(10);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTime receiptCreationDateFrom = OffsetDateTime.now().minusDays(20);
    OffsetDateTime receiptCreationDateTo = OffsetDateTime.now().minusDays(5);
    Pageable pageable = PageRequest.of(0, 10);

    PagedClassificationPaidInstallmentsView mockResult = new PagedClassificationPaidInstallmentsView();

    when(classificationRetrieverServiceMock.getPaidInstallments(
      eq(organizationId),
      eq(assessmentId),
      argThat(f->f.getIuv().equals(iuv)
              && f.getPaymentDateTimeIntervalFilter().getFrom().equals(paymentDateTimeFrom)
              && f.getPaymentDateTimeIntervalFilter().getTo().equals(paymentDateTimeTo)
              && f.getReceiptCreationDateInterval().getFrom().equals(receiptCreationDateFrom)
              && f.getReceiptCreationDateInterval().getTo().equals(receiptCreationDateTo)
              && f.getDebtPositionTypeOrgCode().equals(debtPositionTypeOrgCode)
      ),
      eq(pageable),
      eq(loggedUser),
      eq(accessToken)
    )).thenReturn(mockResult);

    ResponseEntity<PagedClassificationPaidInstallmentsView> response =
      classificationController.getPaidInstallments(
        organizationId,
        debtPositionTypeOrgCode,
        iuv,
        paymentDateTimeFrom,
        paymentDateTimeTo,
        receiptCreationDateFrom,
        receiptCreationDateTo,
        assessmentId,
        pageable
      );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockResult, response.getBody());
  }
}

