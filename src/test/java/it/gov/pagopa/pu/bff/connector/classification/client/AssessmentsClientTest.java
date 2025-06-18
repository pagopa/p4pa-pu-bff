package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AssessmentsClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsControllerApi assessmentsControllerApiMock;

  private AssessmentsClient assessmentsClient;
  private PodamFactory podamFactory;

  @BeforeEach
  void setUp() {
    assessmentsClient = new AssessmentsClient(classificationApisHolderMock);
    podamFactory = new PodamFactoryImpl();
  }

  @AfterEach
  void afterEach() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, assessmentsControllerApiMock);
  }

  @Test
  void givenFiltersWhenFindPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String accessToken = "accessToken";
    AssessmentsFiltersDTO assessmentsFiltersDTO = podamFactory.manufacturePojo(AssessmentsFiltersDTO.class);
    PagedAssessmentsView pagedAssessmentsView = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(classificationApisHolderMock.getAssessmentsControllerApi(accessToken)).thenReturn(assessmentsControllerApiMock);
    Mockito.when(assessmentsControllerApiMock.getPagedAssessmentsList(assessmentsFiltersDTO.getAssessmentName(), assessmentsFiltersDTO.getUpdateDateFrom(), assessmentsFiltersDTO.getUpdateDateTo(), assessmentsFiltersDTO.getIuv(), assessmentsFiltersDTO.getDebtPositionTypeOrgCodes().stream().toList(), assessmentsFiltersDTO.getStatus(), 0, 1, Collections.emptyList())).thenReturn(pagedAssessmentsView);
    //when
    PagedAssessmentsView result = assessmentsClient.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsView.getContent(), result.getContent());
  }
}
