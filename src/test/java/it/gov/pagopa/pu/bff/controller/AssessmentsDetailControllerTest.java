package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsDetailRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailControllerTest {

  @Mock
  private AssessmentsDetailRetrieverService assessmentsDetailRetrieverServiceMock;

  private AssessmentsDetailController assessmentsDetailController;

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
    assessmentsDetailController = new AssessmentsDetailController(assessmentsDetailRetrieverServiceMock);
  }

  @Test
  void whenCreateAssessmentsDetailThenOk() {
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    List<AssessmentsDetail> expectedResult = podamFactory.manufacturePojo(List.class,AssessmentsDetail.class);
    when(assessmentsDetailRetrieverServiceMock.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail, loggedUser, accessToken ))
            .thenReturn(expectedResult);

    ResponseEntity<List<AssessmentsDetail>> result = assessmentsDetailController.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result);
    assertEquals(expectedResult, result.getBody());
  }

}
