package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.client.generated.AssessmentsEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsEntityExtendedClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsEntityExtendedControllerApi assessmentsEntityExtendedControllerApiMock;

  private AssessmentsEntityExtendedClient assessmentsEntityExtendedClient;

  @BeforeEach
  void setUp() {
    assessmentsEntityExtendedClient = new AssessmentsEntityExtendedClient(classificationApisHolderMock);
  }

  @AfterEach
  void afterEach() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, assessmentsEntityExtendedControllerApiMock);
  }

  @Test
  void whenUpdateStatusThenOk() {
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;

    when(classificationApisHolderMock.getAssessmentsEntityExtendedControllerApi(accessToken)).thenReturn(assessmentsEntityExtendedControllerApiMock);
    Mockito.doNothing().when(assessmentsEntityExtendedControllerApiMock).updateStatus(assessmentId,organizationId,status);

    assessmentsEntityExtendedClient.updateStatus(organizationId,assessmentId,status,accessToken);

    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, assessmentsEntityExtendedControllerApiMock);
  }
}
