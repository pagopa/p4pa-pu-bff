package it.gov.pagopa.pu.bff.connector.classification;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationClient;
import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationSearchClient;
import it.gov.pagopa.pu.bff.dto.ClassificationPaidInstallmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceImplTest {

  @Mock
  private ClassificationClient classificationClientMock;
  private ClassificationServiceImpl classificationService;
  @Mock
  private ClassificationSearchClient classificationSearchClientMock;

  @BeforeEach
  void setUp() {
    classificationService = new ClassificationServiceImpl(classificationClientMock, classificationSearchClientMock);
  }

  @Test
  void whenGetTreasuredClassificationThenInvokeClient() {
    Long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedTreasuredClassification expectedPagedTreasuredClassification = new PagedTreasuredClassification();

    when(classificationClientMock.getTreasuredClassifications(Mockito.same(organizationId), Mockito.same(treasuredClassificationFiltersDTO), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedPagedTreasuredClassification);

    PagedTreasuredClassification result = classificationService.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);

    assertSame(expectedPagedTreasuredClassification, result);
  }

  @Test
  void whenGetClassificationDetailThenInvokeClient() {
    Long organizationId = 1L;
    Long classificationId = 123L;
    String accessToken = "ACCESSTOKEN";
    ClassificationDetailViewDTO expectedClassificationDetail = new ClassificationDetailViewDTO();

    when(classificationClientMock.getClassificationDetail(Mockito.same(organizationId), Mockito.same(classificationId), Mockito.same(accessToken)))
      .thenReturn(expectedClassificationDetail);

    ClassificationDetailViewDTO result = classificationService.getClassificationDetail(organizationId, classificationId, accessToken);

    assertSame(expectedClassificationDetail, result);
  }

  @Test
  void whenGetPaidInstallmentsThenInvokeClient() {
    Long organizationId = 1L;
    ClassificationPaidInstallmentsFiltersDTO filters = new ClassificationPaidInstallmentsFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);
    String accessToken = "ACCESSTOKEN";
    PagedClassificationPaidInstallmentsView expectedPagedResult = new PagedClassificationPaidInstallmentsView();

    when(classificationClientMock.getPaidInstallments(
      Mockito.same(organizationId),
      Mockito.same(filters),
      Mockito.same(pageable),
      Mockito.same(accessToken)))
      .thenReturn(expectedPagedResult);

    PagedClassificationPaidInstallmentsView result = classificationService
      .getPaidInstallments(organizationId, filters, pageable, accessToken);

    assertSame(expectedPagedResult, result);
  }
}

