package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkflowTaxonomyServiceImplTest {

  @Mock
  private TaxonomyClient taxonomyClientMock;
  private WorkflowTaxonomyService workflowTaxonomyService;

  @BeforeEach
  void setUp() {
    workflowTaxonomyService = new WorkflowTaxonomyServiceImpl(taxonomyClientMock);
  }

  @Test
  void testSynchronizeTaxonomy() {
    String accessToken = "accessToken";
    WorkflowCreatedDTO expected = new WorkflowCreatedDTO();

    when(taxonomyClientMock.synchronizeTaxonomy(Mockito.same(accessToken)))
      .thenReturn(expected);

    WorkflowCreatedDTO result = workflowTaxonomyService.synchronizeTaxonomy(accessToken);

    assertSame(expected, result);
  }
}
