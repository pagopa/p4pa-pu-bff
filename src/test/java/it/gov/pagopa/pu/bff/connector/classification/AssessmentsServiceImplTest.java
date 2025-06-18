package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
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

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceImplTest {

  @Mock
  private AssessmentsClient assessmentsClientMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsService assessmentsService;

  @BeforeEach
  void setUp() {
    assessmentsService = new AssessmentsServiceImpl(assessmentsClientMock);
  }

  @AfterEach
  void afterEach(){
    Mockito.verifyNoMoreInteractions(assessmentsClientMock);
  }

  @Test
  void givenParametersWhenFindPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String accessToken = "accessToken";
    AssessmentsFiltersDTO assessmentsFiltersDTO = podamFactory.manufacturePojo(AssessmentsFiltersDTO.class);
    PagedAssessmentsView pagedAssessmentsView = podamFactory.manufacturePojo(PagedAssessmentsView.class);
    Mockito.when(assessmentsClientMock.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);

    //when
    PagedAssessmentsView result = assessmentsService.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsView, result);
  }

  @Test
  void givenParametersWhenFindPagedModelAssessmentsDetailThenReturnPagedModelAssessmentsDetail() {
    //given
    String accessToken = "accessToken";
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(PagedModelAssessmentsDetail.class);

    Mockito.when(assessmentsClientMock.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken)).thenReturn(pagedModelAssessmentsDetail);
    //when
    PagedModelAssessmentsDetail result = assessmentsService.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedModelAssessmentsDetail, result);
  }
}
