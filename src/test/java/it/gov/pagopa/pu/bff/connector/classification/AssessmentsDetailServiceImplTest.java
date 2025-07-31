package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsDetailClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
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
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailServiceImplTest {

  @Mock
  private AssessmentsDetailClient assessmentsDetailClientMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsDetailService assessmentsDetailService;

  @BeforeEach
  void setUp() {
    assessmentsDetailService = new AssessmentsDetailServiceImpl(assessmentsDetailClientMock);
  }

  @AfterEach
  void afterEach(){
    Mockito.verifyNoMoreInteractions(assessmentsDetailClientMock);
  }

  @Test
  void whenCreateAssessmentsDetailThenInvokeClient() {
    //given
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    List<AssessmentsDetail> expectedResult = podamFactory.manufacturePojo(List.class, Assessments.class);
    Mockito.when(assessmentsDetailClientMock.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail,accessToken)).thenReturn(expectedResult);

    //when
    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteAssessmentsDetailThenInvokeClient() {
    Long assessmentDetailId = 42L;
    String accessToken = "accessToken";

    assessmentsDetailService.deleteAssessmentsDetails(assessmentDetailId, accessToken);

    Mockito.verify(assessmentsDetailClientMock).deleteAssessmentsDetails(assessmentDetailId, accessToken);
  }
}
