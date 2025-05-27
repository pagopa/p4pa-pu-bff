package it.gov.pagopa.pu.bff.connector.workflow_hub.client;

import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflowhub.controller.generated.TaxonomyApi;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowHubClientTest {
  private WorkflowHubClient workflowHubClient;
  @Mock
  private WorkflowHubApisHolder workflowHubApisHolderMock;
  @Mock
  private TaxonomyApi taxonomyApiMock;

  @BeforeEach
  void setUp() {
    workflowHubClient = new WorkflowHubClient(workflowHubApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(workflowHubApisHolderMock, taxonomyApiMock);
  }

  @Test
  void whenSynchronizeTaxonomyThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    WorkflowCreatedDTO expectedResult = new WorkflowCreatedDTO();

    Mockito.when(workflowHubApisHolderMock.getTaxonomyApi(accessToken))
      .thenReturn(taxonomyApiMock);
    Mockito.when(taxonomyApiMock.synchronizeTaxonomy())
      .thenReturn(expectedResult);

    WorkflowCreatedDTO result = workflowHubClient.synchronizeTaxonomy(accessToken);

    Assertions.assertSame(expectedResult, result);
  }
}
