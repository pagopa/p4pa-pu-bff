package it.gov.pagopa.pu.bff.connector.classification.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class ClassificationClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private ClassificationsApi classificationsApiMock;
  private ClassificationClient classificationClient;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    classificationClient = new ClassificationClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, classificationsApiMock);
  }

  @Test
  void givenValidParamsWhenGetTreasuredClassificationThenReturnExpected() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = podamFactory.manufacturePojo(TreasuredClassificationFiltersDTO.class);
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification expectedPagedTreasuredClassification = new PagedTreasuredClassification();

    when(classificationApisHolderMock.getClassificationsApi(accessToken))
      .thenReturn(classificationsApiMock);

    when(classificationsApiMock.getTreasuredClassifications(organizationId,
      treasuredClassificationFiltersDTO.getLastClassificationDate().getFrom(),
      treasuredClassificationFiltersDTO.getLastClassificationDate().getTo(),
      treasuredClassificationFiltersDTO.getLabel(),
      treasuredClassificationFiltersDTO.getIud(),
      treasuredClassificationFiltersDTO.getIuv(),
      treasuredClassificationFiltersDTO.getIur(),
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
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedPagedTreasuredClassification);

    PagedTreasuredClassification result = classificationClient.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);

    assertSame(expectedPagedTreasuredClassification, result);
  }

  @Test
  void givenValidParamsWhenGetClassificationDetailThenReturnExpectedDetail() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long classificationId = 123L;
    ClassificationDetailViewDTO expectedClassificationDetail = new ClassificationDetailViewDTO();

    when(classificationApisHolderMock.getClassificationsApi(accessToken))
      .thenReturn(classificationsApiMock);

    when(classificationsApiMock.getClassificationDetail(organizationId, classificationId))
      .thenReturn(expectedClassificationDetail);

    ClassificationDetailViewDTO result = classificationClient.getClassificationDetail(organizationId, classificationId, accessToken);

    assertSame(expectedClassificationDetail, result);
  }

  @Test
  void givenNonExistingClassificationWhenClassificationDetailNotFoundThenThrowHttpClientErrorException() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long classificationId = 123L;

    when(classificationApisHolderMock.getClassificationsApi(accessToken))
      .thenReturn(classificationsApiMock);

    when(classificationsApiMock.getClassificationDetail(organizationId, classificationId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ClassificationDetailViewDTO result = classificationClient.getClassificationDetail(organizationId, classificationId, accessToken);

    assertNull(result);
    verify(classificationsApiMock).getClassificationDetail(organizationId, classificationId);
  }
}

