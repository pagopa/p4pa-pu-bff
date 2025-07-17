package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsDetailApi;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailClientTest {

  public static final PodamFactoryImpl podamFactory = new PodamFactoryImpl();
  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsDetailApi assessmentsDetailApiMock;

  private AssessmentsDetailClient assessmentsDetailClient;

  @BeforeEach
  void setUp() {
    assessmentsDetailClient = new AssessmentsDetailClient(classificationApisHolderMock);
  }

  @AfterEach
  void afterEach() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, assessmentsDetailApiMock);
  }

  @Test
  void whenCreateAssessmentsDetailThenOk() {
    //given
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    List<AssessmentsDetail> expectedResult = podamFactory.manufacturePojo(List.class,AssessmentsDetail.class);

    Mockito.when(classificationApisHolderMock.getAssessmentsDetailApi(accessToken)).thenReturn(assessmentsDetailApiMock);
    Mockito.when(assessmentsDetailApiMock.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail)).thenReturn(expectedResult);
    //when
    List<AssessmentsDetail> result = assessmentsDetailClient.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }
}
