package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
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
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = podamFactory.manufacturePojo(TreasuredClassificationFiltersDTO.class);
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification mockPagedTreasuredClassification = new PagedTreasuredClassification();
    when(classificationRetrieverServiceMock.getTreasuredClassification(
      organizationId, treasuredClassificationFiltersDTO, pageable, loggedUser, accessToken))
      .thenReturn(mockPagedTreasuredClassification);

    ResponseEntity<PagedTreasuredClassification> response = classificationController.getTreasuredClassifications(organizationId,
      treasuredClassificationFiltersDTO.getLabel(),
      treasuredClassificationFiltersDTO.getIud(),
      treasuredClassificationFiltersDTO.getIuv(),
      treasuredClassificationFiltersDTO.getIur(),
      treasuredClassificationFiltersDTO.getLastClassificationDate().getFrom(),
      treasuredClassificationFiltersDTO.getLastClassificationDate().getTo(),
      treasuredClassificationFiltersDTO.getPayDate().getFrom(),
      treasuredClassificationFiltersDTO.getPayDate().getTo(),
      treasuredClassificationFiltersDTO.getPaymentDateTime().getFrom(),
      treasuredClassificationFiltersDTO.getPaymentDateTime().getTo(),
      treasuredClassificationFiltersDTO.getRegulationDate().getFrom(),
      treasuredClassificationFiltersDTO.getRegulationDate().getTo(),
      treasuredClassificationFiltersDTO.getBillDate().getFrom(),
      treasuredClassificationFiltersDTO.getBillDate().getTo(),
      treasuredClassificationFiltersDTO.getRegionValueDate().getFrom(),
      treasuredClassificationFiltersDTO.getRegionValueDate().getTo(),
      treasuredClassificationFiltersDTO.getPspCompanyName(),
      treasuredClassificationFiltersDTO.getPspLastName(),
      treasuredClassificationFiltersDTO.getIuf(),
      treasuredClassificationFiltersDTO.getRegulationUniqueIdentifier(),
      treasuredClassificationFiltersDTO.getAccountRegistryCode(),
      treasuredClassificationFiltersDTO.getBillAmountCents(),
      treasuredClassificationFiltersDTO.getRemittanceInformation(),
      treasuredClassificationFiltersDTO.getDebtorFiscalCode(),
      treasuredClassificationFiltersDTO.getDebtPositionTypeOrgCode(),
      treasuredClassificationFiltersDTO.getBillYear(),
      treasuredClassificationFiltersDTO.getBillCode(),
      treasuredClassificationFiltersDTO.getDocumentYear(),
      treasuredClassificationFiltersDTO.getDocumentCode(),
      treasuredClassificationFiltersDTO.getProvisionalAe(),
      treasuredClassificationFiltersDTO.getProvisionalCode(),
      pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockPagedTreasuredClassification, response.getBody());
    verify(classificationRetrieverServiceMock).getTreasuredClassification(
      organizationId, treasuredClassificationFiltersDTO, pageable, loggedUser, accessToken);
  }

  @Test
  void givenCorrectRequestWhenGetClassificationDetailThenOK() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewDTO mockDetailView = new ClassificationDetailViewDTO();
    when(classificationRetrieverServiceMock.getClassificationDetail(
      organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(mockDetailView);

    ResponseEntity<ClassificationDetailViewDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockDetailView, response.getBody());
    verify(classificationRetrieverServiceMock).getClassificationDetail(
      organizationId, classificationId, loggedUser, accessToken);
  }

  @Test
  void givenIncorrectRequestWhenGetClassificationDetailThenNotFound() {
    long organizationId = 1L;
    long classificationId = 999L;

    when(classificationRetrieverServiceMock.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken))
      .thenReturn(null);

    ResponseEntity<ClassificationDetailViewDTO> response = classificationController.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}

